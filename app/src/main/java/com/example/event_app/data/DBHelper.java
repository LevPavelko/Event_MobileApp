package com.example.event_app.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "events_db", null, 1);
        createTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void createTable() {
        String sql = "create table if not exists %s(%s integer primary key autoincrement, %s text, %s text, %s text, %s text)";
        sql = String.format(sql, Events.TABLE_OF_EVENTS, Events.ID, Events.NAME, Events.DATE, Events.PHONE, Events.PLACE );
        getWritableDatabase().execSQL(sql);
    }
    @SuppressLint("Range")
    public List<Event> selectAll() {
        String sql = "select * from " + Events.TABLE_OF_EVENTS;
        List<Event> list = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            Event event;

            while (cursor.moveToNext()) {
                event = new Event(
                        cursor.getInt(cursor.getColumnIndex(Events.ID.toString())),
                        cursor.getString(cursor.getColumnIndex(Events.NAME.toString())),
                        cursor.getString(cursor.getColumnIndex(Events.DATE.toString())),
                       cursor.getString(cursor.getColumnIndex(Events.PHONE.toString())),
                       cursor.getString(cursor.getColumnIndex(Events.PLACE.toString()))

                );
                list.add(event);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public Event insert(Event event) {
        ContentValues values = new ContentValues();

        values.put(Events.NAME.toString(), event.getName());
        values.put(Events.DATE.toString(), event.getDate());
        values.put(Events.PLACE.toString(), event.getPlace());
        values.put(Events.PHONE.toString(), event.getPhone());

        long id = getWritableDatabase()
                .insert(Events.TABLE_OF_EVENTS.toString(), null, values);

        return selectById((int) id);
    }
    @SuppressLint("Range")
    public Event selectById(Integer id) {
        String sql = "select * from %s where %s='%s'";
        sql = String.format(sql, Events.TABLE_OF_EVENTS, Events.ID, id);
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            Event event;
            //while
            if (cursor.moveToNext()) {
                event = new Event(
                        cursor.getInt(cursor.getColumnIndex(Events.ID.toString())),
                        cursor.getString(cursor.getColumnIndex(Events.NAME.toString())),
                        cursor.getString(cursor.getColumnIndex(Events.DATE.toString())),
                        cursor.getString(cursor.getColumnIndex(Events.PHONE.toString())),
                        cursor.getString(cursor.getColumnIndex(Events.PLACE.toString()))
                );
                return event;
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
