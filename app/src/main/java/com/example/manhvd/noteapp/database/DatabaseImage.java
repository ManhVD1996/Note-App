package com.example.manhvd.noteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.manhvd.noteapp.model.Note;

public class DatabaseImage extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "image_list";
    public static final String TABLE_NAME = "image";
    public static final String ID = "id";
    public static final String ID_NOTE = "id_note";

    public static final String PATH = "path";

    private Context mContext;

    public DatabaseImage(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlquery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID       + " integer primary key, "    +
                ID_NOTE  + "INTEGER"                   +
                PATH     + " TEXT)";
        db.execSQL(sqlquery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addImage(Note note) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PATH, note.getPathImage());

        database.insert(TABLE_NAME, null, values);
        database.close();
    }

}
