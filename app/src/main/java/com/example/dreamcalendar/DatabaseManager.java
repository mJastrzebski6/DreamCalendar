package com.example.dreamcalendar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.Editable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE dreamtypes (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'name' TEXT, 'color' TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE dreams (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'name' TEXT, 'type' INTEGER, 'value' TEXT, 'date' TEXT)");

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", "Pragnienie");
        contentValues.put("color", "#ff0000");
        sqLiteDatabase.insert("dreamtypes", null, contentValues);

        contentValues.put("name", "Koszmar");
        contentValues.put("color", "#800080");
        sqLiteDatabase.insert("dreamtypes", null, contentValues);

        contentValues.put("name", "Bez sensu");
        contentValues.put("color", "#ffd700");
        sqLiteDatabase.insert("dreamtypes", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS dreamtypes");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS dreams");

        onCreate(sqLiteDatabase);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    public ArrayList<Dream> getDreamsByValue(String value){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM dreamtypes" , null);

        ArrayList<DreamType> dreamTypes = new ArrayList<>();

        while(result.moveToNext()){
            dreamTypes.add( new DreamType(
                    result.getString(result.getColumnIndex("name")),
                    result.getString(result.getColumnIndex("color"))
            ));
        }

        result = db.query(true, "dreams", new String[] { "type", "value", "date" }, "value" + " LIKE ?",
                new String[] {"%"+ value+ "%" }, null, null, null, null);

        ArrayList<Dream> dreams = new ArrayList<>();

        while(result.moveToNext()){
            String type = result.getString(result.getColumnIndex("type"));

            String color = "#ffffff";

            for(DreamType dtype : dreamTypes){
                if(dtype.getName().equals(type)) color = dtype.getColor();
            }

            dreams.add( new Dream(
                    result.getString(result.getColumnIndex("value")),
                    result.getString(result.getColumnIndex("type")),
                    result.getString(result.getColumnIndex("date")),
                    color
            ));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Collections.sort(dreams, (s1, s2) -> LocalDate.parse(s1.getDate(), formatter).
                compareTo(LocalDate.parse(s2.getDate(), formatter)));
        return dreams;

    }

    public void deleteType(String type){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("dreamtypes", "name=?", new String[]{type});
    }

    public int getNumberOfDreamsByType(String type){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM dreams WHERE type=?" ,  new String [] {type});
        return result.getCount();
    }

    public Boolean insertDream(String value, String type, String date){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("value", value);
        contentValues.put("type", type);
        contentValues.put("date", date);

        long result = db.insert("dreams", null, contentValues);
        db.close();

        if(result == -1) return false;
        else return true;
    }

    public int checkIfNoteExists(String dateString){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM dreams WHERE date=?" ,  new String [] {dateString});
        return result.getCount();
    }

    public void updateNote(String value, String type, String date){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("value",value);
        cv.put("type",type);

        db.update("dreams", cv, "date = ?", new String[]{date});
    }

    public void deleteDream(String dateString){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("dreams", "date=?", new String[]{dateString});
    }

    @SuppressLint("Range")
    public Dream getDreamData(String dateString){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT dreams.type,dreams.value,dreams.date, dreamtypes.color FROM dreams JOIN dreamtypes ON dreams.type=dreamtypes.name WHERE date=?" ,  new String [] {dateString});

        ArrayList<Dream> dream = new ArrayList<>();

        while(result.moveToNext()){
            dream.add( new Dream(
                    result.getString(result.getColumnIndex("value")),
                    result.getString(result.getColumnIndex("type")),
                    result.getString(result.getColumnIndex("date")),
                    result.getString(result.getColumnIndex("color"))
            ));
        }
        return dream.get(0);
    }

    public Boolean insertDreamType(String name, String color){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("color", color);

        long result = db.insert("dreamtypes", null, contentValues);
        db.close();

        if(result == -1) return false;
        else return true;
    }

    @SuppressLint("Range")
    public ArrayList<Dream> getDreams(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT dreams.type,dreams.value,dreams.date, dreamtypes.color FROM dreams JOIN dreamtypes ON dreams.type=dreamtypes.name" , null);

        ArrayList<Dream> dreams = new ArrayList<>();

        while(result.moveToNext()){
            dreams.add( new Dream(
                    result.getString(result.getColumnIndex("value")),
                    result.getString(result.getColumnIndex("type")),
                    result.getString(result.getColumnIndex("date")),
                    result.getString(result.getColumnIndex("color"))
            ));
        }
        return dreams;
    }

    @SuppressLint("Range")
    public ArrayList<DreamType> getDreamTypes(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM dreamtypes" , null);

        ArrayList<DreamType> dreamTypes = new ArrayList<>();

        while(result.moveToNext()){
            dreamTypes.add( new DreamType(
                    result.getString(result.getColumnIndex("name")),
                    result.getString(result.getColumnIndex("color"))
            ));
        }
        return dreamTypes;
    }

    @SuppressLint("Range")
    public String getDreamColor(String date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT dreamtypes.color as color FROM dreamtypes JOIN dreams ON dreams.type=dreamtypes.name WHERE dreams.date=?" , new String [] {date});

        ArrayList<String> dreams = new ArrayList<>();

        while(result.moveToNext()){
            dreams.add(result.getString(result.getColumnIndex("color")));
        }

        if(dreams.size()==1) {
            return dreams.get(0);
        }
        else return "-1";
    }
}
