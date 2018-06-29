package com.fortests.meet9practice2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotepadBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "notepadBase.db";

    public NotepadBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + NotepadDbSchema.NotepadTable.NAME +
            "(" + " _id integer primary key autoincrement, " +
            NotepadDbSchema.NotepadTable.Cols.NOTE_NAME + ", " +
            NotepadDbSchema.NotepadTable.Cols.TIME + ", " +
            NotepadDbSchema.NotepadTable.Cols.CONTENT +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
