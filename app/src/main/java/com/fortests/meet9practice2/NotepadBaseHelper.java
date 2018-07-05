package com.fortests.meet9practice2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    // everything below - added

    private static ContentValues getContentValues(Note note){
        ContentValues values = new ContentValues();
        values.put(NotepadDbSchema.NotepadTable.Cols.NOTE_NAME, note.getName());
        values.put(NotepadDbSchema.NotepadTable.Cols.TIME, note.getTime().getTime());
        values.put(NotepadDbSchema.NotepadTable.Cols.CONTENT, note.getContent());
        return values;
    }

    public long addNote(Note note){
        SQLiteDatabase database = null;
        long id = 0;
        try {
            database = this.getWritableDatabase();
            ContentValues contentValues = getContentValues(note);
            database.beginTransaction();
            id = database.insert(NotepadDbSchema.NotepadTable.NAME,null,contentValues);
            database.setTransactionSuccessful();
        } catch (SQLException e){
            Log.v("SQLiteExeption", e.getMessage());
        } finally {
            if (database !=null){
                if (database.inTransaction()){
                    database.endTransaction();
                }
                database.close();
            }
        }
        return id;
    }

    public Note getNote(int id) {
        Note note = new Note();
        SQLiteDatabase database = null;
        try{
            database = this.getReadableDatabase();
            database.beginTransaction();
            String whereClause = "_id = ?";
            //SQlite stars with 1, arraylist with 0
            String[] whereArgs = new String[]{String.valueOf(id)};
            Cursor cursor = database.query(NotepadDbSchema.NotepadTable.NAME,null,whereClause,whereArgs,null,null,null);
            if (cursor.moveToFirst()){
                note.setName(cursor.getString(cursor.getColumnIndex(NotepadDbSchema.NotepadTable.Cols.NOTE_NAME)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NotepadDbSchema.NotepadTable.Cols.CONTENT)));
                note.setTime(new Date(cursor.getLong(cursor.getColumnIndex(NotepadDbSchema.NotepadTable.Cols.TIME))));
            }
            cursor.close();
            database.setTransactionSuccessful();
        } catch (SQLException e){
            Log.v("SQLiteExeption", e.getMessage());
        } finally {
            if (database !=null){
                if (database.inTransaction()){
                    database.endTransaction();
                }
                database.close();
            }
        }
        return note;
    }

    public List<Note> getNotepad(){
        List<Note> notepad = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = this.getReadableDatabase();
            database.beginTransaction();
            Cursor cursor = database.query(NotepadDbSchema.NotepadTable.NAME,null,null,null,null,null,null);
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    Note note = new Note();
                    note.setName(cursor.getString(cursor.getColumnIndex(NotepadDbSchema.NotepadTable.Cols.NOTE_NAME)));
                    note.setContent(cursor.getString(cursor.getColumnIndex(NotepadDbSchema.NotepadTable.Cols.CONTENT)));
                    note.setTime(new Date(cursor.getLong(cursor.getColumnIndex(NotepadDbSchema.NotepadTable.Cols.TIME))));
                    notepad.add(note);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            database.setTransactionSuccessful();
        } catch (SQLException e){
            Log.v("SQLiteExeption", e.getMessage());
        } finally {
            if (database !=null){
                if (database.inTransaction()){
                    database.endTransaction();
                }
                database.close();
            }
        }
        return notepad;
    }

    public int updateNote(int id, Note note){
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id + 1)};
        int i = 0;
        SQLiteDatabase database = null;
        try {
            database = this.getWritableDatabase();
            ContentValues contentValues = getContentValues(note);
            database.beginTransaction();
            i = database.update(NotepadDbSchema.NotepadTable.NAME,contentValues,whereClause,whereArgs);
            //database.insert(NotepadDbSchema.NotepadTable.NAME,null,contentValues);
            database.setTransactionSuccessful();
        } catch (SQLException e){
            Log.v("SQLiteExeption", e.getMessage());
        } finally {
            if (database !=null){
                if (database.inTransaction()){
                    database.endTransaction();
                }
                database.close();
            }
        }
        return i;
    }
}
