package com.lps.rsdc2010.common.lang;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A collection of common date and time related tasks
 */
public class DateTime
{
    /**
     * Returns the time this Calendar represents as a formatted string according to {@link http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html}
     * 
     * @param c
     *            The calendar object to convert into a date / time string.
     * @param format
     *            The formatting string to use - see {@link http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html}
     * @return The time the calendar object represents, formatted according to the formatting string.
     */
    public static String formatCalendar(Calendar c, String format)
    {
        SimpleDateFormat commonDateFormat = new SimpleDateFormat(format);
        GregorianCalendar twoDigitYears = new GregorianCalendar();
        twoDigitYears.set(Calendar.YEAR, 2000);
        twoDigitYears.set(Calendar.MONTH, Calendar.JANUARY);
        twoDigitYears.set(Calendar.DAY_OF_MONTH, 1);
        twoDigitYears.set(Calendar.HOUR_OF_DAY, 0);
        twoDigitYears.set(Calendar.MINUTE, 0);
        commonDateFormat.set2DigitYearStart(twoDigitYears.getTime());
        commonDateFormat.setTimeZone(c.getTimeZone());
        return commonDateFormat.format(c.getTime());
    }
    
}
