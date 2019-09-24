package co.arctern.api.provider.util;

import org.joda.time.LocalDateTime;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * date util for all date and time conversions and manipulations.
 */
public interface DateUtil {

    public static Timestamp CURRENT_TIMESTAMP = new Timestamp(new Date(System.currentTimeMillis()).getTime());

    public static Timestamp CURRENT_MIDNIGHT_TIMESTAMP = new Timestamp(LocalDateTime.now().toLocalDate().toDateTimeAtStartOfDay().getMillis());

    public static Long fetchDifferenceFromCurrentDateInMs(Integer minutes) {
        return new Date(System.currentTimeMillis() - minutes * 60 * 1000).getTime();
    }

    public static Timestamp fetchTimestampFromCurrentTimestamp(Integer minutes) {
        return new Timestamp(new Date(System.currentTimeMillis() - 20 * 60 * 1000).getTime());
    }

    public static Timestamp zonedDateTimeToTimestampConversion(ZonedDateTime value) {
        return Timestamp.valueOf(value.toLocalDateTime());
    }

}
