package co.arctern.api.provider.util;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * date util for all date and time conversions and manipulations.
 */
@Service
public class DateUtil {

    /**
     * fetch current timestamp.
     */
    public Timestamp CURRENT_TIMESTAMP = new Timestamp(new Date().getTime());

    /**
     * fetch current timestamp from zonedDateTime.
     *
     * @param dateTime
     * @return
     */
    public static Timestamp fetchTodayTimestamp(ZonedDateTime dateTime) {
        return Timestamp.valueOf(dateTime.toLocalDate().atStartOfDay());
    }

    /**
     * fetch time (in ms) with a difference of given minutes.
     *
     * @param minutes
     * @return
     */
    public static Long fetchDifferenceFromCurrentDateInMs(Integer minutes) {
        return new Date(System.currentTimeMillis() - minutes * 60 * 1000).getTime();
    }

    /**
     * fetch timeStamp (in ms) with a difference of given minutes.
     *
     * @param minutes
     * @return
     */
    public static Timestamp fetchTimestampFromCurrentTimestamp(Integer minutes) {
        return new Timestamp(new Date(System.currentTimeMillis() - minutes * 60 * 1000).getTime());
    }

    /**
     * convert zonedDateTime to timeStamp.
     *
     * @param value
     * @return
     */
    public static Timestamp zonedDateTimeToTimestampConversion(ZonedDateTime value) {
        return Timestamp.valueOf(value.toLocalDateTime());
    }

    public static Timestamp convertGmtToIst(Timestamp time) {
        if (time != null) {
            return zonedDateTimeToTimestampConversion(time.toLocalDateTime().atZone(ZoneId.of("Asia/Kolkata")));
        }
        return null;
    }

}
