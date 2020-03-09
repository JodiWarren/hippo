package uk.nhs.digital.externalstorage.workflow.externalFilePublish;

import static org.hippoecm.repository.HippoStdNodeType.PUBLISHED;
import static uk.nhs.digital.ps.PublicationSystemConstants.INDEX_FILE_NAME;

import org.hippoecm.repository.api.WorkflowException;
import org.onehippo.repository.documentworkflow.DocumentHandle;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.workflow.AbstractExternalFileTask;
import uk.nhs.digital.ps.PublicationSystemConstants;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class ExternalFilePublishTask extends AbstractExternalFileTask {

    private static final String DOCUMENTS_ROOT_FOLDER = "/content/documents";
    public static final ZoneId LONDON_ZONE_ID = ZoneId.of("Europe/London");
    public static final int HOUR_OF_PUBLICATION_RELEASE = 9;
    public static final int MINUTE_OF_PUBLICATION_RELEASE = 30;
    static final String TAG_PUBLICATION_DATE = "PUBLICATION_DATE";
    static final String TAG_EARLY_ACCESS_KEY = "EARLY_ACCESS_KEY";

    private final transient Clock clock;

    private static final Logger LOG = LoggerFactory.getLogger(ExternalFilePublishTask.class);

    public ExternalFilePublishTask(Clock clock) {
        super();
        this.clock = clock;
    }

    protected void processResourceNodes(final PooledS3Connector s3Connector, final NodeIterator resourceNodes) throws RepositoryException, WorkflowException {
        Node variantNode = getVariant().getNode(getWorkflowContext().getInternalWorkflowSession());

        if (isPublication(variantNode)) {
            // if publishing publication we need to check "PUBLICLY ACCESSIBLE" flag, and also update all datasets
            setResourcePermissionOnPublication(s3Connector, resourceNodes);
        } else if (isDataset(variantNode)) {
            // when publishing dataset, we need to check parent LIVE publication state
            publishResources(resourceNodes, getS3JobForDataset(s3Connector, variantNode));
        } else {
            // no additional logic for other document types. Published == public attachments
            publishResources(resourceNodes, s3Connector::publishResource);
        }
    }

    private void publishResources(NodeIterator resourceNodes, Consumer<String> s3Job) throws RepositoryException {
        for (Node node; resourceNodes.hasNext(); ) {
            node = resourceNodes.nextNode();
            if (node.hasProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE)) {
                String externalResource = node
                    .getProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE)
                    .getString();

                try {
                    s3Job.accept(externalResource);
                } catch (Exception ex) {
                    LOG.error("Error while publishing S3 resource: {}", externalResource);
                }
            }
        }
    }

    private void setResourcePermissionOnPublication(PooledS3Connector s3Connector,
        NodeIterator resourceNodes) throws RepositoryException {
        Node variantNode = getVariant()
            .getNode(getWorkflowContext().getInternalWorkflowSession());

        Consumer<String> s3Job = getS3JobBasedOnPublicationDate(s3Connector,
            getPublicationTags(variantNode));

        publishResources(resourceNodes, s3Job);

        // now find all datasets and publish resources
        // only if publication name is "content"
        if (INDEX_FILE_NAME.equals(variantNode.getName())) {
            NodeIterator nodes = findPublicationDatasetsVariant(variantNode,
                PUBLISHED);

            while (nodes.hasNext()) {
                publishResources(findResourceNodes(nodes.nextNode()), s3Job);
            }
        }
    }

    private Consumer<String> getS3JobForDataset(PooledS3Connector s3Connector,
        Node datasetVariant)
        throws RepositoryException, WorkflowException {
        DocumentVariant parentPublication = findPublicationInFolder(
            datasetVariant.getParent().getParent());
        if (parentPublication == null) {
            // no parent publication found
            return s3Connector::publishResource;
        }

        Map<String, String> tags = getPublicationTags(parentPublication
            .getNode(getWorkflowContext().getInternalWorkflowSession()));
        return getS3JobBasedOnPublicationDate(s3Connector, tags);
    }

    private Consumer<String> getS3JobBasedOnPublicationDate(
        PooledS3Connector s3Connector, Map<String, String> tags) {
        Consumer<String> s3Job;

        if (Long.parseLong(tags.get(TAG_PUBLICATION_DATE)) <= Instant.now(clock)
            .getEpochSecond()) {
            s3Job = ref -> {
                s3Connector.publishResource(ref);
                s3Connector.tagResource(ref, tags);
            };
        } else {
            s3Job = ref -> {
                s3Connector.unpublishResource(ref);
                s3Connector.tagResource(ref, tags);
            };
        }

        return s3Job;
    }

    private Map<String, String> getPublicationTags(Node publicationVariant)
        throws RepositoryException {
        Map<String, String> tags = new HashMap<>();

        Calendar pubDate = publicationVariant
            .getProperty(PublicationSystemConstants.PROPERTY_PUBLICATION_DATE)
            .getDate();
        long pubDateEpoch = pubDate.toInstant().atZone(LONDON_ZONE_ID)
            .toOffsetDateTime().withHour(HOUR_OF_PUBLICATION_RELEASE)
            .withMinute(MINUTE_OF_PUBLICATION_RELEASE).withSecond(0)
            .toEpochSecond();
        tags.put(TAG_PUBLICATION_DATE, String.valueOf(pubDateEpoch));

        String earlyAccessKey = publicationVariant
            .getProperty(PublicationSystemConstants.PROPERTY_EARLY_ACCESS_KEY)
            .getString();
        tags.put(TAG_EARLY_ACCESS_KEY, earlyAccessKey);
        return tags;
    }

    private DocumentVariant findPublicationInFolder(Node folder) throws RepositoryException, WorkflowException {
        if (DOCUMENTS_ROOT_FOLDER.equals(folder.getPath())) {
            return null;
        }

        NodeIterator nodes = folder.getNodes();
        while (nodes.hasNext()) {
            Node node = nodes.nextNode();
            if (isContentHandle(node)) {
                DocumentHandle documentHandle = new DocumentHandle(node);
                documentHandle.initialize();

                return documentHandle.getDocuments().get(variantState);
            }
        }

        return findPublicationInFolder(folder.getParent());
    }

    private boolean isContentHandle(Node node) throws RepositoryException {
        return node.isNodeType("hippo:handle")
            && INDEX_FILE_NAME.equals(node.getName());
    }

    private boolean isDataset(Node variantNode) throws RepositoryException {
        return  variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_DATASET);
    }

    private boolean isPublication(Node variantNode) throws RepositoryException {
        return variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_PUBLICATION)
            || variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_LEGACY_PUBLICATION);
    }
}
