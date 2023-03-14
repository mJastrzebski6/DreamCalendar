package com.example.dreamcalendar.ui.note;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class NoteActivity extends FragmentActivity {
    String date="coś się zepsulo";
    String dbDate = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, new NoteFragment()).commit();
        }

        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date");
        dbDate = bundle.getString("dbDate");
    }
    public String getDate() {
        return date;
    }

    public String getDbDate() {
        return dbDate;
    }

    @Override
    public void onBackPressed() {

    }
}
