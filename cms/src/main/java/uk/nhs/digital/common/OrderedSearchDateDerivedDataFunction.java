package uk.nhs.digital.common;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import org.apache.jackrabbit.value.LongValue;
import org.hippoecm.repository.ext.DerivedDataFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.externalstorage.workflow.externalFilePublish.ExternalFilePublishTask;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import javax.jcr.Value;

/**
 * Derived data function that turns the nominal publication date into a long that when
 * sorted, will have all published documents first in descending order followed by
 * upcoming documents in ascending date order
 */
public class OrderedSearchDateDerivedDataFunction extends DerivedDataFunction {

    private static final long serialVersionUID = 1;
    private static final Logger log = LoggerFactory.getLogger(OrderedSearchDateDerivedDataFunction.class);

    @Override
    public Map<String, Value[]> compute(Map<String, Value[]> parameters) {
        try {
            Value[] dateValues = parameters.get("date");
            if (isEmpty(dateValues)) {
                return emptyMap();
            }

            Calendar date = dateValues[0].getDate();

            long orderedSearchDate = date.getTimeInMillis();
            if (!isPublic(date)) {
                orderedSearchDate *= -1;
            }

            parameters.put("orderedSearchDate", new Value[]{new LongValue(orderedSearchDate)});

            return parameters;
        } catch (Exception ex) {
            log.error("Failed to compute Ordered Search Date", ex);

            return emptyMap();
        }
    }

    private boolean isPublic(Calendar publicationDate) {
        LocalDateTime publicationDateTime = publicationDate.toInstant()
            .atZone(ExternalFilePublishTask.LONDON_ZONE_ID).toLocalDateTime()
            .withHour(ExternalFilePublishTask.HOUR_OF_PUBLICATION_RELEASE).withMinute(
                ExternalFilePublishTask.MINUTE_OF_PUBLICATION_RELEASE)
            .withSecond(0);

        LocalDateTime currentDateTime = LocalDateTime.now(ExternalFilePublishTask.LONDON_ZONE_ID);

        return !currentDateTime.isBefore(publicationDateTime);
    }
}
