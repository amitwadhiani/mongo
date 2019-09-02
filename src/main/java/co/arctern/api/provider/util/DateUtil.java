package co.arctern.api.provider.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * date util for all date and time conversions and manipulations.
 */
public interface DateUtil {

    public static Timestamp CURRENT_TIMESTAMP = new Timestamp(new Date(System.currentTimeMillis()).getTime());

    public static Long fetchDifferenceFromCurrentDateInMs(Integer minutes) {
        return new Date(System.currentTimeMillis() - minutes * 60 * 1000).getTime();
    }
}
