package com.example.dreamcalendar;

import android.app.Activity;

import java.util.ArrayList;

public class Helpers {
    public static String[] getDreamTypes(Activity activity){
        DatabaseManager db = new DatabaseManager (
                activity,
                "dreamCalendar.db",
                null,
                1
        );
        ArrayList<DreamType> dreamTypes = db.getDreamTypes();

        db.close();

        String[] paths = new String[dreamTypes.size()];

        for(int i = 0; i < dreamTypes.size(); i++) {
            paths[i] = dreamTypes.get(i).getName();
        }
        return paths;
    }
}