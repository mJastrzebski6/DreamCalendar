package com.example.dreamcalendar.ui.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamcalendar.DatabaseManager;
import com.example.dreamcalendar.Dream;
import com.example.dreamcalendar.R;

import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private String monthAndYear;
    private Activity activity;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, String monthAndYear, Activity activity)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.monthAndYear = monthAndYear;
        this.activity = activity;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        holder.dayOfMonth.setText(daysOfMonth.get(position));

        if(daysOfMonth.get(position).equals("")) return;

        DatabaseManager db = new DatabaseManager (
                activity,
                "dreamCalendar.db",
                null,
                1
        );

        String dayString = "";
        if(Integer.parseInt(daysOfMonth.get(position))<10) dayString = "0"+ daysOfMonth.get(position);
        else dayString = daysOfMonth.get(position);

        String res =  db.getDreamColor(dayString+"-"+monthAndYear);

        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View w = inflater.inflate(R.layout.calendar_cell, null);
        ConstraintLayout cl = w.findViewById(R.id.cl);

        if(!res.equals("-1")){
            holder.dayOfMonth.setTextColor(Color.parseColor(res));
        }
        db.close();
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }

    private String getDreamColor(){
        return "";
    }
}