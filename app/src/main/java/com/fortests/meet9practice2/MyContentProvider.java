package com.fortests.meet9practice2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY =
            "com.fortests.meet9practice2.MyContentProvider";
    private static final String NOTE_TABLE = NotepadDbSchema.NotepadTable.NAME;
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + NOTE_TABLE);

    public static final int NOTEPAD = 1;
    public static final int NOTE_ID = 2;

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, NOTE_TABLE, NOTEPAD);
        sURIMatcher.addURI(AUTHORITY, NOTE_TABLE + "/#",
                NOTE_ID);
    }

    private NotepadBaseHelper mNotepadBaseHelper;

    @Override
    public boolean onCreate() {
        mNotepadBaseHelper = new NotepadBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(NOTE_TABLE);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case NOTE_ID:
                queryBuilder.appendWhere("_id" + " = "
                        + uri.getLastPathSegment());
                break;
            case NOTEPAD:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(mNotepadBaseHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id = 0;
        switch (uriType) {
            case NOTEPAD:
                Note note = new Note();
                note.setName(values.getAsString("name"));
                note.setContent(values.getAsString("content"));
                note.setTime(new Date(values.getAsLong("time")));
                id = mNotepadBaseHelper.addNote(note);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(NOTE_TABLE + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case NOTE_ID:
                String id = uri.getLastPathSegment();
                Note note = new Note();
                note.setName(values.getAsString("name"));
                note.setContent(values.getAsString("content"));
                note.setTime(new Date(values.getAsLong("time")));
                rowsUpdated = mNotepadBaseHelper.updateNote(Integer.valueOf(id), note);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +
                        uri);
        }
        getContext().getContentResolver().notifyChange(uri,
                null);
        return rowsUpdated;
    }
}
