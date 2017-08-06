package com.party.funhunt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ermike on 4/13/2017.
 */

public class Utilities {

    public static String getDateWithFormat(String format, String date) {
        //Formats yyyy-MM-dd hh:mm:ss , For month in string MMM
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date from = dateFormat.parse(date);
            return new SimpleDateFormat(format).format(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
