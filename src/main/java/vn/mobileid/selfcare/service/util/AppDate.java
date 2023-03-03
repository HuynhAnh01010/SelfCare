/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.selfcare.service.util;

import static java.lang.Math.log;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppDate {

    public static String FORMAT_FULL_ddMMyyyyHHmmss = "dd-MM-yyyy HH:mm:ss";

    public static final String FORMAT_DATE_ddMMyyyy = "dd-MM-yyyy";

    public static void main(String[] args) {
        Date d = new Date();
        System.out.println("Date: " + strDate2timestamp("24-07-2019", FORMAT_DATE_ddMMyyyy));
    }

    public static Timestamp getTimeStampForMysql(Date time) {
        if (time == null) {
            return null;
        }
        return new java.sql.Timestamp(time.getTime());
    }

    public static Timestamp strDate2timestamp(String dateTime, String format) {
        if (format == null || "".equals(format)) {
            format = FORMAT_FULL_ddMMyyyyHHmmss;
        }
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        try {
            Date date = (dateTime == null || "".equals(dateTime)) ? null : sformat.parse(dateTime);
//            date == null ? 
            return (date == null) ? null : new Timestamp(date.getTime());
        } catch (ParseException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public static String convert2ddMMyyy(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_ddMMyyyy);
        if (date != null) {
            return format.format(date);
        }
        return "";
    }

    public static String convertDate2Str(Date date, String fm) {
        if (fm == null || "".equals(fm)) {
            fm = FORMAT_DATE_ddMMyyyy;
        }
        SimpleDateFormat format = new SimpleDateFormat(fm);
        if (date != null) {
            return format.format(date);
        }
        return "";
    }

    public static String convert2ddMMyyyMinusDays(LocalDate date, int param, String format) {

        if (date == null) {
            return "";
        }
        LocalDate newDate = date.minusDays(param);
        if (format == null || "".equals(format)) {
            format = FORMAT_DATE_ddMMyyyy;
        }
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(format);
        String text = newDate.format(formatters);

        return text;
    }

    public static Date convert2Date(String dateTime, String format) {
//    SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");  
//    SimpleDateFormat formatter2=new SimpleDateFormat("dd-MMM-yyyy");  
//    SimpleDateFormat formatter3=new SimpleDateFormat("MM dd, yyyy");  
//    SimpleDateFormat formatter4=new SimpleDateFormat("E, MMM dd yyyy");  
//    SimpleDateFormat formatter5=new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss"); 
        if (format == null || "".equals(format)) {
            format = "dd-MM-yyyy";
        }
//        SimpleDateFormat sformat = new SimpleDateFormat(format);
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sformat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = (dateTime == null || "".equals(dateTime)) ? null : sformat.parse(dateTime);
            return date;
        } catch (ParseException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public static Date returnDateFromMysqlDate(Timestamp tsp) {
        if (tsp == null) {
            return null;
        }
        return new Date(tsp.getTime());
    }

    public static String convertDateSqlToJava(Timestamp tsp, String format) {
        if (tsp == null) {
            return null;
        }
        if (format == null) {
            format = "dd-MM-yyyy HH:mm:ss";
        }
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        Date dta = new Date(tsp.getTime());
        Date date = new Date(tsp.getTime());
        return sformat.format(date);
    }

}
