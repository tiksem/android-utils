package com.dbbest.android.formatting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * User: mda
 */
public class DateUtils {

    public static final String ISO_8601_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'hh:mm:ssz";

    public static final String ROUND_DATE_FORMAT = "EEEE, MMMM d";
    public static final String ROUND_TIME_FORMAT = "h:mm a";
    public static final String ROUND_TIME_DATE_PATTERN = "h:mma EE. MMM. d";
    public static final String BIRTH_DATE_FORMAT = "y/MM/dd";

    private static final String NOT_AVAILABLE = "--";

    private static SimpleDateFormat sIso8601Formatter = new SimpleDateFormat(ISO_8601_TIMESTAMP_PATTERN, Locale.ENGLISH);

    public static Date parseIso8601Timestamp(String timeStamp) {
        if (timeStamp == null) {
            return null;
        }
        timeStamp = timeStamp.replaceFirst("Z", "+00:00");
        Date date = null;
        try {
            date = sIso8601Formatter.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateAsString(Date date, String dateFormat) {
        if (date == null) {
            return NOT_AVAILABLE;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static String formatIso8601Timestamp(String timeStamp, String dateFormat) {
        return getDateAsString(parseIso8601Timestamp(timeStamp), dateFormat);
    }
}
