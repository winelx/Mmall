package mmall.util;

import com.mysql.jdbc.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {
    private static  final String SRANDARD__FORMAT="yyyy-MM-dd HH:mm:ss";

    public static Date strToDate(String dataTimeStr, String formatStr) {
        DateTimeFormatter dataTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dataTimeFormatter.parseDateTime(dataTimeStr);
        return dateTime.toDate();
    }
    public static String DateToStr(Date date, String formatstr) {
            if (date==null){
                return org.apache.commons.lang3.StringUtils.EMPTY;
            }
            DateTime dateTime=new DateTime(date);
            return dateTime.toString(formatstr);
    }

    public static Date strToDate(String dataTimeStr) {
        DateTimeFormatter dataTimeFormatter = DateTimeFormat.forPattern(SRANDARD__FORMAT);
        DateTime dateTime = dataTimeFormatter.parseDateTime(dataTimeStr);
        return dateTime.toDate();
    }

    public static String DateToStr(Date date) {
        if (date==null){
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(SRANDARD__FORMAT);
    }
}
