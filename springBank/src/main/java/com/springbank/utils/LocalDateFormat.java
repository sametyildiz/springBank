package com.springbank.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LocalDateFormat {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.getDefault());

    public static String localDate(Calendar calendar) {
        return dateFormat.format(calendar.getTime());
    }
}
