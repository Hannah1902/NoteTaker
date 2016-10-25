package com.example.hannah.notetaker;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Hannah on 10/14/16.
 * Activity class providing functionality for sharing of notes from a database
 */
public class ShareNotesActivity extends ListActivity {

    DatabaseManager dbManager;
    NoteTaker nt;
    Cursor noteCursor;
    ListAdapter noteList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_notes);

        nt = new NoteTaker();

        dbManager = new DatabaseManager(this);
        try {
            SQLiteDatabase db = dbManager.getWritableDatabase();
        } catch (SQLException se) {
            Toast.makeText(this, se.getMessage(), Toast.LENGTH_LONG).show();
        }

        noteCursor = dbManager.getCursor();

        displayNotes();
        registerForContextMenu(getListView());

    }

    public void displayNotes() {
        noteList = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item, noteCursor,
                new String [] {DatabaseManager.DATE, DatabaseManager.SUBJECT, DatabaseManager.BODY},
                new int [] {android.R.id.text1, android.R.id.text2}, 0);

        setListAdapter(noteList);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, dbManager.getSubjectFromDatabase(id) + "\n"
                            + dbManager.getBodyFromDatabase(id));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_with)));
    }

}
