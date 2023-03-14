package com.example.dreamcalendar.ui.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dreamcalendar.DatabaseManager;
import com.example.dreamcalendar.Helpers;
import com.example.dreamcalendar.R;

public class NoteFragment extends Fragment {
    View root;
    TextView dateTV;
    EditText mainET;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_note, container, false);
        dateTV = root.findViewById(R.id.datetv);
        TextView goBackTV = root.findViewById(R.id.goback_tv);
        mainET = root.findViewById(R.id.maintv);

        goBackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitDialog();
            }
        });

        TextView saveTV = root.findViewById(R.id.save_tv);

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mainET.getText().toString();
                if (TextUtils.isEmpty(str) || (str = str.trim()).length() == 0) {
                    Toast.makeText(getActivity(), "Napisz cokolwiek", Toast.LENGTH_SHORT).show();
                }
                else showSaveDialog();
            }
        });


        NoteActivity activity = (NoteActivity) getActivity();
        String date = activity.getDate();

        dateTV.setText(date);

        return root;
    }


    private void showSaveDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Podaj kategorie snu");

        String[] paths = Helpers.getDreamTypes(getActivity());
        alert.setItems(paths, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // wyswietl opcje[which]);
                DatabaseManager db = new DatabaseManager (
                        getActivity(),
                        "dreamCalendar.db",
                        null,
                        1
                );


                NoteActivity activity = (NoteActivity) getActivity();
                db.insertDream(mainET.getText().toString(), paths[which], activity.getDbDate());

                db.close();
                getActivity().finish();

            }
        });

        alert.show();
    }

    private void showExitDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Uwaga!");
        alert.setMessage("Czy na pewno chcesz wyjść?");
        alert.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }

        });

        alert.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
}
