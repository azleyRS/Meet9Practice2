package com.fortests.meet9practice2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBManager {
    private NotepadBaseHelper mNotepadBaseHelper;
    public DBManager(Context context){
        this.mNotepadBaseHelper = new NotepadBaseHelper(context);
    }

    public void addNote(Note note){
        SQLiteDatabase database = null;
        try {
            database = mNotepadBaseHelper.getWritableDatabase();
            ContentValues contentValues = getContentValues(note);
            database.beginTransaction();
            database.insert(NotepadDbSchema.NotepadTable.NAME,null,contentValues);
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
    }

    public Note getNote(int id){
        Note note = null;
        SQLiteDatabase database = null;
        try{
            database = mNotepadBaseHelper.getReadableDatabase();
            database.beginTransaction();
            String whereClause = "_id = ?";
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
            database = mNotepadBaseHelper.getReadableDatabase();
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

    private static ContentValues getContentValues(Note note){
        ContentValues values = new ContentValues();
        values.put(NotepadDbSchema.NotepadTable.Cols.NOTE_NAME, note.getName());
        values.put(NotepadDbSchema.NotepadTable.Cols.TIME, note.getTime().getTime());
        values.put(NotepadDbSchema.NotepadTable.Cols.CONTENT, note.getContent());
        return values;
    }
}
