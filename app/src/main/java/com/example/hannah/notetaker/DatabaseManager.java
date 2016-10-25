package com.example.hannah.notetaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Hannah on 10/1/16.
 *
 */
public class DatabaseManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "StoredNotes";
    public static final int DATABASE_VERSION = 1;
    public static final String NOTES_TABLE = "tbl_history";
    public static final String ID = "_id";
    public static final String DATE = "date";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    private final int DATE_INDEX = 1;
    private final int SUBJECT_INDEX = 2;
    private final int BODY_INDEX = 3;
    private Context appContext;

    public DatabaseManager (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        appContext = context;
    }

    public void onCreate (SQLiteDatabase db) {

        String sqlCreate = "create table " + NOTES_TABLE + " ( "
                + ID + " integer primary key autoincrement, "
                + DATE + " text, "
                + SUBJECT + " text, "
                + BODY + " text"
                + ")";

        try {
            db.execSQL(sqlCreate);
        }
        catch  (SQLException se) {
            Toast.makeText(appContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Inserts notes into database
     * @param date
     * @param subject
     * @param body
     */
    public void insertNote (String date, String subject, String body) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DATE, date);
            values.put(SUBJECT, subject);
            values.put(BODY, body);

            long newId = db.insert(NOTES_TABLE, null, values);
            db.close();

            if (newId == -1) {
                Toast.makeText(appContext, "Unsuccessful save", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(appContext, "Note saved", Toast.LENGTH_LONG).show();
            }
        }
        catch (SQLException se) {
            Toast.makeText(appContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Selects all notes within the database
     * @return noteList ArrayList of notes
     */
    public ArrayList<String> selectAll() {
        ArrayList <String> noteList = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "Select * from " + NOTES_TABLE;
            Cursor c = db.rawQuery(query, null);

            c.moveToFirst();

            while (!c.isAfterLast()) {
                String singleRecord = "";

                for (int i = 1; i < c.getColumnCount(); i++) {
                    singleRecord += c.getString(i) + " ";
                }

                noteList.add(singleRecord);
                c.moveToNext();
            }
            c.close();
        }
        catch (SQLException sqe) {
            Toast.makeText(appContext, sqe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return noteList;
    }

    /**
     * Retrieves the cursor for the database
     * @return c cursor
     */
    public Cursor getCursor() {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * from " + NOTES_TABLE;
        Cursor c = db.rawQuery(query, null);

        return c;
    }

    /**
     * Retrieves cursor based on filters set by the user
     * @param name
     * @param value
     * @return c cursor with user-set filters
     */
    public Cursor getFilteredCursor(String name, String value) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(NOTES_TABLE, null, name + "=?",
                new String [] {value}, null, null, name);

        return c;
    }

    /**
     * Retrieves the body of the note based on id
     * @param id id of note
     * @return body the text body of the note
     */
    public String getBodyFromDatabase (Long id) {
        String body = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.query(NOTES_TABLE, new String[]{ID, DATE, SUBJECT, BODY}, ID + "=" + id,
                    null, null, null, null);

            c.moveToFirst();

            body = c.getString(BODY_INDEX);

            c.close();
        }
        catch (SQLException sqe) {
            Toast.makeText(appContext, sqe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return body;
    }

    /**
     * Retrieves subject of note based on id
     * @param id id of note
     * @return subject the text subject of note
     */
    public String getSubjectFromDatabase (Long id) {
        String subject = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.query(NOTES_TABLE, new String[]{ID, DATE, SUBJECT, BODY}, ID + "=" + id,
                    null, null, null, null);

            c.moveToFirst();

            subject = c.getString(SUBJECT_INDEX);

            c.close();
        }
        catch (SQLException sqe) {
            Toast.makeText(appContext, sqe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return subject;
    }

    /**
     * Retrieves the date of note based on id
     * @param id the id of the note
     * @return date the date of the note entry
     */
    public String getDateFromDatabase (Long id) {
        String date = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.query(NOTES_TABLE, new String[]{ID, DATE, SUBJECT, BODY}, ID + "=" + id,
                    null, null, null, null);

            c.moveToFirst();

            date = c.getString(DATE_INDEX);

            c.close();
        }
        catch (SQLException sqe) {
            Toast.makeText(appContext, sqe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return date;
    }

    /**
     * Selects notes based on id
     * @param id id of note
     * @return noteList ArrayList of note content
     */
    public ArrayList<String> selectById (long id) {
        ArrayList<String> noteList = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.query(NOTES_TABLE, new String[]{ID, DATE, SUBJECT, BODY}, ID + "=" + id,
                    null, null, null, null);

            c.moveToFirst();

            while (!c.isAfterLast()) {
                String singleRecord = "";

                //i starts at 1 to skip showing the id
                for (int i = 1; i < c.getColumnCount(); i++) {
                    singleRecord += c.getString(i) + " ";
                }

                noteList.add(singleRecord);
                c.moveToNext();
            }
            c.close();
        }
        catch (SQLException sqe) {
            Toast.makeText(appContext, sqe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return noteList;
    }

    public ArrayList<String> selectByColumn(String columnName, String columnValue) {
        ArrayList <String> noteList = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.query(NOTES_TABLE, null, columnName + "=?",
                    new String [] {columnValue}, null, null, columnName);

            c.moveToFirst();

            while (!c.isAfterLast()) {
                String singleRecord = "";

                //i starts at 1 to skip showing the id
                for (int i = 1; i < c.getColumnCount(); i++) {
                    singleRecord += c.getString(i) + " ";
                }

                noteList.add(singleRecord);
                c.moveToNext();
            }
            c.close();
        }
        catch (SQLException sqe) {
            Toast.makeText(appContext, sqe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return noteList;
    }

    public ArrayAdapter<String> fillAutoCompleteTextFields(Context context, String column) {
        ArrayAdapter<String> adapter = null;

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.query(false, NOTES_TABLE, new String [] {column}, null, null, null,
                    null, null, null);

            int numOfRecords = c.getCount();

            if (numOfRecords > 0) {
                c.moveToFirst();

                String [] autoTextOptions = new String [numOfRecords];

                for (int i = 0; i < numOfRecords; i++) {
                    autoTextOptions[i] = c.getString(c.getColumnIndex(column));
                    c.moveToNext();
                }

                adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,
                        autoTextOptions);

                db.close();
            }
        }
        catch (SQLException sqe) {
            Toast.makeText(null, sqe.getMessage(), Toast.LENGTH_LONG).show();
        }
        return adapter;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(appContext, "Upgrade successful", Toast.LENGTH_LONG).show();
    }
}
