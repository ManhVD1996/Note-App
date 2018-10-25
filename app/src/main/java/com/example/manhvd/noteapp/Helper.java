package com.example.manhvd.noteapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {

    public static class sendDataByMain {
        public static final String NEED_REFRESH = "needRefresh";
        public static final String NOTE_DATA = "Note_Data";
        public static final String NOTE_LIST = "Note_List";
        public static final String NOTE = "Note_";
        public static final String TITLE_NOTE = "Title_Note";
        public static final String BODY_NOTE = "Body_Note";
        public static final String TIME_NOTE = "Time_Note";
    }

    public static class Color {
        public static final String WHILE = "#FFFFFF";
        public static final String ORANGE = "#e17345";
        public static final String GREEN = "#45e14a";
        public static final String BLUE = "#5884ff";
        public static final String COLOR_DEFAULT = "#ffffff";
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String setTime(String time) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = null;
        String format =  "dd/MM hh:mm";
        timeFormat = new SimpleDateFormat(format);
        time = timeFormat.format(calendar.getTime());
        return time;
    }

    public static String setTimeFull(String time) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = null;
        String strDateFormat = "dd/MM/yyyy hh:mm:ss";
        timeFormat = new SimpleDateFormat(strDateFormat);
        time = timeFormat.format(calendar.getTime());
        return time;
    }
}
