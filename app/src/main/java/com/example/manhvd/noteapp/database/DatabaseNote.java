package com.example.manhvd.noteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.manhvd.noteapp.activity.AddActivity;
import com.example.manhvd.noteapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseNote extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "note_list";
    private static final String TABLE_NAME = "notes";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String BODY = "body";
    private static final String TIME = "time";
    private static final String TIME_FULL = "time_full";
    private static final String PATH = "path";
    private static final String COLOR = "color";

    private Context mContext;
    private AddActivity mAddActivity;

    public DatabaseNote(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlquery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID        + " integer primary key, " +
                TITLE     + " TEXT, "                +
                BODY      + " TEXT, "                +
                TIME      + " TEXT, "                +
                TIME_FULL + " TEXT, "                +
                COLOR     + " TEXT)";
        db.execSQL(sqlquery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, note.getTitle());
        values.put(BODY, note.getBody());
        values.put(TIME, note.getTime());
        values.put(TIME_FULL, note.getTimeFull());
        values.put(COLOR, note.getColor());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Note getNoteById(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, new String[]{ID, TITLE, BODY, TIME, TIME_FULL, COLOR}, ID + " =?",
                new String[]{ String.valueOf(id) }, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        Note note = new Note(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        note.setId(id);
        cursor.close();
        database.close();
        return note;
    }

    public int update(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(BODY, note.getBody());
        values.put(TIME, note.getTime());
        values.put(TIME_FULL, note.getTimeFull());
        values.put(COLOR, note.getColor());
        return database.update(TABLE_NAME, values, ID + " =?", new String[]{String.valueOf(note.getId())});
    }

    public List<Note> getAllNote() {
        List<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT * FROM  " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setBody(cursor.getString(2));
                note.setTime(cursor.getString(3));
                note.setTimeFull(cursor.getString(4));
                note.setColor(cursor.getString(5));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return noteList;
    }

    public void deleteNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        database.close();
    }

    public int getNoteCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int count =  cursor.getCount();
        cursor.close();
        return count;
    }
}
