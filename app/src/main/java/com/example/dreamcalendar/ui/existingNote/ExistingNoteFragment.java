package com.example.dreamcalendar.ui.existingNote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
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

public class ExistingNoteFragment extends Fragment {
    View root;
    TextView dateTV;
    EditText mainET;
    TextView deleteTV;
    TextView saveTV;
    ExistingNoteActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         activity = (ExistingNoteActivity) getActivity();

        root = inflater.inflate(R.layout.fragment_existing_note, container, false);

        dateTV = root.findViewById(R.id.datetv);
        deleteTV = root.findViewById(R.id.delete_tv);
        saveTV = root.findViewById(R.id.save_tv);

        TextView goBackTV = root.findViewById(R.id.goback_tv);
        mainET = root.findViewById(R.id.maintv);

        mainET.setText(activity.getDreamData().getValue());

        goBackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitDialog();
            }
        });

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mainET.getText().toString();
                if (TextUtils.isEmpty(str) || (str = str.trim()).length() == 0) {
                    Toast.makeText(getActivity(), "Napisz cokolwiek", Toast.LENGTH_SHORT).show();
                }else showSaveDialog();
            }
        });

        deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

        String date = activity.getDate();
        dateTV.setText(date);

        return root;
    }

    private void deleteDream(){
        DatabaseManager db = new DatabaseManager (
                getActivity(),
                "dreamCalendar.db",
                null,
                1
        );

        db.deleteDream(activity.getDbDate());

        db.close();
        getActivity().finish();
    }
    private void showDeleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Uwaga");
        alert.setMessage("Czy na pewno chcesz usunąć sen?");

        alert.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteDream();
            }
        });

        alert.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { }
        });
        alert.show();
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


                ExistingNoteActivity activity = (ExistingNoteActivity) getActivity();
                db.updateNote(mainET.getText().toString(), paths[which], activity.getDbDate());

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
            public void onClick(DialogInterface dialog, int which) { }
        });
        alert.show();
    }
}
