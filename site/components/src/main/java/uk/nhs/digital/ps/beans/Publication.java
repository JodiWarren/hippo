package uk.nhs.digital.ps.beans;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.pagination.Paginated;
import uk.nhs.digital.pagination.Pagination;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;
import uk.nhs.digital.ps.directives.DateFormatterDirective;
import uk.nhs.digital.website.beans.Infographic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publication")
public class Publication extends PublicationBase implements Paginated {

    private static final int HOUR_OF_PUBLIC_RELEASE = 9;
    private static final int MINUTE_OF_PUBLIC_RELEASE = 30;

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.SUMMARY)
    public StructuredText getSummary() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.SUMMARY);

        return new StructuredText(getProperty(PublicationBase.PropertyKeys.SUMMARY, ""));
    }

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.KEY_FACTS)
    public StructuredText getKeyFacts() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.KEY_FACTS);

        return new StructuredText(getProperty(PublicationBase.PropertyKeys.KEY_FACTS, ""));
    }

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.KEY_FACTS_HEAD)
    public HippoHtml getKeyFactsHead() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.KEY_FACTS_HEAD);

        return getHippoHtml(PublicationBase.PropertyKeys.KEY_FACTS_HEAD);
    }

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.KEY_FACT_IMAGES)
    public List<ImageSection> getKeyFactImages() {
        assertPropertyPermitted(PropertyKeys.KEY_FACT_IMAGES);

        return getChildBeansIfPermitted(PropertyKeys.KEY_FACT_IMAGES, ImageSection.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.KEY_FACT_INFOGRAPHICS)
    public List<Infographic> getKeyFactInfographics() {
        assertPropertyPermitted(PropertyKeys.KEY_FACT_INFOGRAPHICS);

        return getChildBeansIfPermitted(PropertyKeys.KEY_FACT_INFOGRAPHICS, Infographic.class);
    }

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.KEY_FACTS_TAIL)
    public HippoHtml getKeyFactsTail() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.KEY_FACTS_HEAD);

        return getHippoHtml(PublicationBase.PropertyKeys.KEY_FACTS_TAIL);
    }

    public List<PublicationPage> getPages() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.PAGES);

        return getParentBean().getChildBeans(PublicationPage.class);
    }

    public Boolean getBeforePublicationDate() {
        Calendar publicationDate = getProperty(PropertyKeys.NOMINAL_DATE);
        if (publicationDate == null) {
            return false;
        }
        LocalDateTime publicationDateTime = publicationDate.toInstant()
            .atZone(DateFormatterDirective.TIME_ZONE.toZoneId()).toLocalDateTime()
            .withHour(HOUR_OF_PUBLIC_RELEASE).withMinute(MINUTE_OF_PUBLIC_RELEASE)
            .withSecond(0);

        LocalDateTime currentDateTime = LocalDateTime
            .now(DateFormatterDirective.TIME_ZONE.toZoneId());

        return currentDateTime.isBefore(publicationDateTime);
    }

    public List<IndexPage> getPageIndex() {
        List<IndexPage> pages = new ArrayList<>(getPages());

        if (!pages.isEmpty()) {
            // Add the Publication itself as the first link
            pages.add(0, new IndexPageImpl("Overview", this));
        }

        return pages;
    }

    @Override
    public Pagination paginate() {
        return new Pagination(null, getPageIndex().stream().skip(1).findFirst().orElse(null));
    }

    public Boolean isCorrectAccessKey(String accessKey) {
        return StringUtils
            .isNotBlank(getProperty(PublicationBase.PropertyKeys.EARLY_ACCESS_KEY))
            && getProperty(PublicationBase.PropertyKeys.EARLY_ACCESS_KEY)
            .equals(accessKey);
    }
}
