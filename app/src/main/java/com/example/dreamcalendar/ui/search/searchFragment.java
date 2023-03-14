package com.example.dreamcalendar.ui.search;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcalendar.DatabaseManager;
import com.example.dreamcalendar.Dream;
import com.example.dreamcalendar.R;
import com.example.dreamcalendar.ui.home.HomeViewModel;

import java.util.ArrayList;

public class searchFragment extends Fragment {
    View root;
    ListView dream_lv;
    EditText dream_et;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        dream_lv = root.findViewById(R.id.dreams_lv);
        dream_et = root.findViewById(R.id.dream_et);

        Button searchButton = root.findViewById(R.id.search_btn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                CreateListView();
            }
        });


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CreateListView(){

        DatabaseManager db = new DatabaseManager (
                getActivity(),
                "dreamCalendar.db",
                null,
                1
        );

        ArrayList<Dream> dreams = db.getDreamsByValue(dream_et.getText().toString());

        db.close();

        DreamsArrayAdapter adapter = new DreamsArrayAdapter (
                getActivity(),
                R.layout.dream_row_layout,
                dreams,
                dream_et.getText().toString()
        );
        dream_lv.setAdapter(adapter);
    }
}