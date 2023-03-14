package com.example.dreamcalendar.ui.search;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamcalendar.Dream;
import com.example.dreamcalendar.R;

import java.util.ArrayList;
import java.util.List;

public class DreamsArrayAdapter extends ArrayAdapter {

    private ArrayList<Dream> _list;
    private Context _context;
    private int _resource;
    private String _substring;

    public DreamsArrayAdapter(@NonNull Context context, int resource, ArrayList<Dream> objects, String substring) {
        super(context, resource, objects);

        this._list= objects;
        this._context = context;
        this._resource = resource;
        this._substring = substring.toLowerCase();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);

        TextView date_tv = convertView.findViewById(R.id.date_tv);
        TextView value_tv = convertView.findViewById(R.id.value_tv);

        String value = _list.get(position).getValue();
        value = value.trim();
        String output = value;
        value = value.toLowerCase();

        date_tv.setText(_list.get(position).getDate());

        int lengthOfFragment = 80;
        int index = value.indexOf(_substring);
        int startIndex = index<lengthOfFragment ? 0 : index-lengthOfFragment;
        int endIndex = index+lengthOfFragment<value.length() ? index+lengthOfFragment : value.length();

        value_tv.setText(output.substring(startIndex, endIndex));

        date_tv.setTextColor(Color.parseColor(_list.get(position).getColor()));

        return convertView;
    }

    @Override
    public int getCount() {
        return _list.size();
    }

}
