package com.example.dreamcalendar.ui.existingNote;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.dreamcalendar.DatabaseManager;
import com.example.dreamcalendar.Dream;
import com.example.dreamcalendar.DreamType;


public class ExistingNoteActivity extends FragmentActivity {
    String date="coś się zepsulo";
    String dbDate = "";
    Dream dream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ExistingNoteFragment()).commit();
        }

        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date");
        dbDate = bundle.getString("dbDate");

        DatabaseManager db = new DatabaseManager (
                this,
                "dreamCalendar.db",
                null,
                1
        );

        dream = db.getDreamData(dbDate);
        db.close();
    }

    public String getDate() {
        return date;
    }
    public String getDbDate() {
        return dbDate;
    }
    public Dream getDreamData(){return dream;}
    @Override
    public void onBackPressed() {

    }
}
