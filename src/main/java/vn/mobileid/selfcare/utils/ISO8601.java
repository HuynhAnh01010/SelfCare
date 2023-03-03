package vn.mobileid.selfcare.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class ISO8601 {
    /** Transform Calendar to ISO 8601 string. */
    public static String fromDate(final Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmssX");
        df.setTimeZone(tz);
        return  df.format(date);
//
////        System.out.println(nowAsISO);

//        Date date = calendar.getTime();
//        String formatted = new SimpleDateFormat("yyyyMMdd'T'HHmmssX")
//                .format(date);
////        return formatted.substring(0, 22) + ":" + formatted.substring(22);
//        return formatted;
    }

    /** Get current date and time formatted as ISO 8601 string. */
    public static String now() {
        return fromDate(new  Date());
    }

    /** Transform ISO 8601 string to Calendar. */
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }
}
