package com.example.dreamcalendar.ui.dreamtypes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcalendar.DatabaseManager;
import com.example.dreamcalendar.DreamType;
import com.example.dreamcalendar.Helpers;
import com.example.dreamcalendar.R;
import com.example.dreamcalendar.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class DreamTypesFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    private LinearLayout colorsLayout;
    private EditText dreamCategoryNameEditText;
    private Spinner dreamTypeSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dream_types, container, false);
        colorsLayout = root.findViewById(R.id.types_color_layout);
        dreamCategoryNameEditText =  root.findViewById(R.id.dream_type_text_edit);

        initializeSpinner();
        initializeColorSelect();
        initializeButtons();

        return root;
    }

    public void initializeButtons(){
        Button add_dream_type_button = root.findViewById(R.id.add_dream_type_button);

        add_dream_type_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager db = new DatabaseManager (
                        getActivity(),
                        "dreamCalendar.db",
                        null,
                        1
                );
                String color  = String.format("#%06X", (0xFFFFFF & dreamCategoryNameEditText.getCurrentTextColor()));
                Boolean result = db.insertDreamType(dreamCategoryNameEditText.getText().toString(), color);

                if(result){
                    Toast.makeText(getActivity(), "Dodano kategorie", Toast.LENGTH_SHORT).show();
                    dreamCategoryNameEditText.setText("");
                    dreamCategoryNameEditText.setTextColor(Color.parseColor("#ff0000"));
                    dreamCategoryNameEditText.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    Toast.makeText(getActivity(), "Błąd podczas dodawania kategorii", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

        Button delete_dream_type_button = root.findViewById(R.id.delete_dream_type_button);

        delete_dream_type_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeleteDialog();
            }
        });
    }

    public Boolean deleteType(){
        DatabaseManager db = new DatabaseManager (
                getActivity(),
                "dreamCalendar.db",
                null,
                1
        );
        String value = dreamTypeSpinner.getSelectedItem().toString();
        int numOfRows = db.getNumberOfDreamsByType(value);
        Boolean needToReload = false;

        if(numOfRows == 0){
                db.deleteType(value);
                needToReload = true;
        }
        else{
            Toast.makeText(getActivity(), "Nie można usunąć. Istnieją sny z tą kategorią!", Toast.LENGTH_LONG).show();
        }

        db.close();
        return needToReload;
    }

    public void openDeleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Uwaga!");
        alert.setMessage("Czy na pewno chcesz usunąć kategorię?");
        alert.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Boolean needToReload = deleteType();
                if(needToReload){
                   initializeSpinner();
                }

            }
        });

        alert.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    public void initializeSpinner(){
        dreamTypeSpinner = root.findViewById(R.id.type_spinner);
        String[] paths = Helpers.getDreamTypes(getActivity());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dreamTypeSpinner.setAdapter(adapter);
    }

    public void initializeColorSelect(){

        ArrayList<String> colors = new ArrayList<>(Arrays.asList("#ff0000", "#ffa500", "#ffd700" , "#00ff00","#008000", "#009a80" ,"#00ffff", "#3399ff","#800080"));

        for (String color : colors) {
            LinearLayout ll1 = new LinearLayout(getActivity());
            ll1.setLayoutParams(new LinearLayout.LayoutParams(100,100));
            ll1.setBackgroundColor(Color.parseColor(color));
            ll1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    dreamCategoryNameEditText.setTextColor(Color.parseColor(color));
                    dreamCategoryNameEditText.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
                }
            });
            colorsLayout.addView(ll1);
        }

    }

}
