package com.example.dreamcalendar.ui.calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamcalendar.DatabaseManager;
import com.example.dreamcalendar.Dream;
import com.example.dreamcalendar.R;
import com.example.dreamcalendar.ui.existingNote.ExistingNoteActivity;
import com.example.dreamcalendar.ui.gallery.GalleryViewModel;
import com.example.dreamcalendar.ui.note.NoteActivity;
import com.example.dreamcalendar.ui.note.NoteFragment;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;


public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private View root;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        Button prevButton = root.findViewById(R.id.previousMonth);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousMonthAction(view);
            }
        });

        Button nextButton = root.findViewById(R.id.nextMonth);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMonthAction(view);
            }
        });

        DatabaseManager db = new DatabaseManager (
                getActivity(),
                "dreamCalendar.db",
                null,
                1
        );

        ArrayList<Dream> dreams = db.getDreams();

        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_calendar, container, false);
        return root;
    }

    private void initWidgets()
    {
        calendarRecyclerView = root.findViewById(R.id.calendarRecyclerView);
        monthYearText = root.findViewById(R.id.monthYearTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthNameYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, getMonthAndYear(selectedDate), getActivity());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue()-1;

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthNameYearFromDate(LocalDate date)
    {
        ArrayList<String> months = new ArrayList<>(Arrays.asList("Styczeń", "Luty", "Marzec" , "Kwiecień", "Maj", "Czerwiec","Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"));
        int monthNum = date.getMonthValue()-1;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return months.get(monthNum) +" "+ date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getMonthAndYear(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            DatabaseManager db = new DatabaseManager (
                    getActivity(),
                    "dreamCalendar.db",
                    null,
                    1
            );
            String dayString = dayText;
            if(Integer.parseInt(dayText) < 10) dayString="0"+dayText;


            int numOfRows = db.checkIfNoteExists(dayString + "-" +getDate(selectedDate));
            db.close();
            if(numOfRows == 0){
                Intent i = new Intent(getContext(), NoteActivity.class);
                i.putExtra("date", dayText + " " + monthYearFromDate(selectedDate));
                i.putExtra("dbDate", dayString + "-" +getDate(selectedDate));
                startActivity(i);
            }
            else{
                Intent i = new Intent(getContext(), ExistingNoteActivity.class);
                i.putExtra("date", dayText + " " + monthYearFromDate(selectedDate));
                i.putExtra("dbDate", dayString + "-" +getDate(selectedDate));
                startActivity(i);
            }
        }
    }
}
