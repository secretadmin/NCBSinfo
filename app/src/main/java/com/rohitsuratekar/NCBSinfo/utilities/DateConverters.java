package com.rohitsuratekar.NCBSinfo.utilities;

import android.util.Log;

import com.rohitsuratekar.NCBSinfo.constants.DateFormats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * All date conversions are handled through this class.
 * Standard timings are given in DateFormats enum
 */
public class DateConverters {

    private final String TAG = getClass().getSimpleName();



    public String convertToString(Date dateFormat, DateFormats format) {
        return new SimpleDateFormat(format.getFormat(), Locale.getDefault()).format(dateFormat);
    }

    public String convertToString(Calendar dateFormat, DateFormats format) {
        return new SimpleDateFormat(format.getFormat(), Locale.getDefault()).format(dateFormat.getTime());
    }

    /**
     * @param stringFormat : Input format
     * @return : Formatted Date (uses current time if default field is missing)
     */
    public Date convertToDate(String stringFormat) {
        DateFormats dateFormat = detectFormat(stringFormat);
        if (dateFormat != null) {
            if (dateFormat.isDateComplete() && dateFormat.isTimeComplete()) {
                try {
                    return new SimpleDateFormat(detectFormat(stringFormat).getFormat(), Locale.getDefault()).parse(stringFormat);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse date in convertToDate() " + stringFormat);
                }
            } else if (!dateFormat.isTimeComplete()) {
                try {

                    return new SimpleDateFormat(
                            detectFormat(stringFormat).getFormat() + " " + DateFormats.TIME_12_HOURS_STANDARD.getFormat(), Locale.getDefault())
                            .parse(stringFormat + " " + getDefaultTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse string in convertToDate() : " + stringFormat);
                }
            } else if (!dateFormat.isDateComplete()) {

                try {
                    return new SimpleDateFormat(
                            DateFormats.DATE_STANDARD.getFormat() + " " + detectFormat(stringFormat).getFormat(), Locale.getDefault())
                            .parse(getDefaultDate() + " " + stringFormat);

                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse string in convertToDate() : " + stringFormat);
                }

            }
        } else {
            Log.e(TAG, "Failed to parse date in convertToDate()");
        }
        return null;
    }


    /**
     * @param stringFormat : Uses this format
     * @return : Returns calendar (uses current time if default field is missing)
     */

    public Calendar convertToCalendar(String stringFormat) {

        DateFormats dateFormat = detectFormat(stringFormat);
        if (dateFormat != null) {
            if (dateFormat.isDateComplete() && dateFormat.isTimeComplete()) {
                try {
                    Date date = new SimpleDateFormat(detectFormat(stringFormat).getFormat(), Locale.getDefault()).parse(stringFormat);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    return calendar;
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse date in convertToCalender() " + stringFormat);
                }
            } else if (!dateFormat.isTimeComplete()) {
                try {
                    Date date = new SimpleDateFormat(
                            detectFormat(stringFormat).getFormat() + " " + DateFormats.TIME_12_HOURS_STANDARD.getFormat(), Locale.getDefault())
                            .parse(stringFormat + " " + getDefaultTime());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    return calendar;
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse string in convertToCalender() : " + stringFormat);
                }
            } else if (!dateFormat.isDateComplete()) {

                try {
                    Date date = new SimpleDateFormat(
                            DateFormats.DATE_STANDARD.getFormat() + " " + detectFormat(stringFormat).getFormat(), Locale.getDefault())
                            .parse(getDefaultDate() + " " + stringFormat);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    return calendar;
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse string in convertToCalender() : " + stringFormat);
                }

            }
        } else {
            Log.e(TAG, "Failed to parse date in convertToCalender()");
        }
        return null;
    }

    /**
     * @param input        : Input String
     * @param targetFormat : Target format you want
     * @return : It will add current time or date if it is not present in given input
     */
    public String convertFormat(String input, DateFormats targetFormat) {
        DateFormats dateFormat = detectFormat(input);
        if (dateFormat != null) {
            if (dateFormat.isDateComplete() && dateFormat.isTimeComplete()) {

                try {
                    Date date = new SimpleDateFormat(detectFormat(input).getFormat(), Locale.getDefault()).parse(input);
                    return new SimpleDateFormat(targetFormat.getFormat(), Locale.getDefault()).format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse string in convertFormat() : " + input);
                }
            } else if (!dateFormat.isTimeComplete()) {

                try {
                    Date date = new SimpleDateFormat(
                            detectFormat(input).getFormat() + " " + DateFormats.TIME_12_HOURS_STANDARD.getFormat(), Locale.getDefault())
                            .parse(input + " " + getDefaultTime());
                    return new SimpleDateFormat(targetFormat.getFormat(), Locale.getDefault()).format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse string in convertFormat() : " + input);
                }
            } else if (!dateFormat.isDateComplete()) {

                try {
                    Date date = new SimpleDateFormat(
                            DateFormats.DATE_STANDARD.getFormat() + " " + detectFormat(input).getFormat(), Locale.getDefault())
                            .parse(getDefaultDate() + " " + input);
                    return new SimpleDateFormat(targetFormat.getFormat(), Locale.getDefault()).format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse string in convertFormat() : " + input);
                }

            }
        } else {
            Log.e(TAG, "Failed to parse string in convertFormat()");
        }
        return null;
    }

    /**
     * @param givenString : Checks this string
     * @return : Returns Dateformat enum with respected Dateformat
     */
    public DateFormats detectFormat(String givenString) {
        givenString = givenString.trim();
        int numberOfSpaces = givenString.split("\\s+").length;
        int numberOfSlash = givenString.split("/").length;
        int numberOfColons = givenString.split(":").length;
        int numberOfChars = givenString.replaceAll("\\s+", "").length();

        for (DateFormats d : DateFormats.values()) {
            int Spaces = d.getFormat().split("\\s+").length;
            int Slash = d.getFormat().split("/").length;
            int Colons = d.getFormat().split(":").length;
            int Chars = d.getFormat().replaceAll("\\s+", "").length();

            if (numberOfSpaces == Spaces && numberOfSlash == Slash && numberOfColons == Colons && numberOfChars == Chars) {
                return d;
            }
        }
        Log.e(TAG, "Can not find pattern for :" + givenString);
        return null;
    }

    /**
     * Converting 3 line code into single liner
     *
     * @param date : Input
     * @return : Returns calender
     */
    public Calendar DateToCalender(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    private String getDefaultDate() {
        return new SimpleDateFormat(DateFormats.DATE_STANDARD.getFormat(), Locale.getDefault()).format(new Date());
    }

    private String getDefaultTime() {
        return new SimpleDateFormat(DateFormats.TIME_12_HOURS_STANDARD.getFormat(), Locale.getDefault()).format(new Date());
    }

}
