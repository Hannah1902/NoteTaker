package com.example.hannah.notetaker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Hannah on 10/11/16.
 * Activity class allowing for the viewing and searching of notes in a database
 *
 */
public class ViewNotesActivity extends ListActivity {

    DatabaseManager dbManager;
    ListAdapter noteList;
    Cursor noteCursor;
    NoteTaker nt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

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

        ArrayAdapter dateAdapter = dbManager.fillAutoCompleteTextFields(this, DatabaseManager.DATE);
        if (dateAdapter != null) {
            AutoCompleteTextView dateEntry = (AutoCompleteTextView) findViewById(R.id.date_entry);
            dateEntry.setAdapter(dateAdapter);
        }

        ArrayAdapter subjectAdapter = dbManager.fillAutoCompleteTextFields(this, DatabaseManager.SUBJECT);
        if (subjectAdapter != null) {
            AutoCompleteTextView subjectEntry = (AutoCompleteTextView) findViewById(R.id.subject_entry);
            subjectEntry.setAdapter(subjectAdapter);
        }

    }

    /**
     * Displays notes in a ListView using customized SimpleCursorAdapter
     */
    public void displayNotes() {

        final ListView list = (ListView) findViewById(android.R.id.list);

        noteList = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item, noteCursor,
                new String [] {DatabaseManager.DATE, DatabaseManager.SUBJECT, DatabaseManager.BODY},
                new int [] {android.R.id.text1, android.R.id.text2}, 0);

        setListAdapter(noteList);
    }

    /**
     * Searches the database by column name
     * @param view
     */
    public void searchBySelection(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        RadioButton dateRadio = (RadioButton) findViewById(R.id.filter_date);
        RadioButton subjectRadio = (RadioButton) findViewById(R.id.filter_subject);
        AutoCompleteTextView dateEntry = (AutoCompleteTextView) findViewById(R.id.date_entry);
        AutoCompleteTextView subjectEntry = (AutoCompleteTextView) findViewById(R.id.subject_entry);

        if (dateRadio.isChecked()) {
            String columnValue = dateEntry.getText().toString();

            if (columnValue.isEmpty()) {
                Toast.makeText(this, R.string.enter_a_date, Toast.LENGTH_LONG).show();
            }
            else {
                noteCursor = dbManager.getFilteredCursor(DatabaseManager.DATE, columnValue);
            }
        }
        else if (subjectRadio.isChecked()) {
            String columnValue = subjectEntry.getText().toString();

            if (columnValue.isEmpty()) {
                Toast.makeText(this, R.string.enter_a_subject, Toast.LENGTH_LONG).show();
            }
            else {
                noteCursor = dbManager.getFilteredCursor(DatabaseManager.SUBJECT, columnValue);
            }
        }

        noteList = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item, noteCursor,
                new String [] {DatabaseManager.DATE, DatabaseManager.SUBJECT, DatabaseManager.BODY},
                new int [] {android.R.id.text1, android.R.id.text2}, 0);

        setListAdapter(noteList);
    }

    /**
     * Disables the other search box when one radio button is selected
     * @param view
     */
    public void disableOtherSelection(View view) {
        AutoCompleteTextView dateEntry = (AutoCompleteTextView) findViewById(R.id.date_entry);
        AutoCompleteTextView subjectEntry = (AutoCompleteTextView) findViewById(R.id.subject_entry);
        ViewGroup.LayoutParams dateParams = dateEntry.getLayoutParams();
        ViewGroup.LayoutParams subjectParams = subjectEntry.getLayoutParams();


        if (((RadioButton)view).isChecked()) {
            switch (view.getId()) {
                case R.id.filter_date:
                    subjectEntry.setText("");
                    dateParams.height = 100;
                    dateEntry.setLayoutParams(dateParams);
                    dateEntry.setVisibility(View.VISIBLE);
                    subjectParams.height = 0;
                    subjectEntry.setLayoutParams(subjectParams);
                    break;
                case R.id.filter_subject:
                    dateEntry.setText("");
                    subjectParams.height = 100;
                    subjectEntry.setLayoutParams(subjectParams);
                    subjectEntry.setVisibility(View.VISIBLE);
                    dateParams.height = 0;
                    dateEntry.setLayoutParams(dateParams);
                    break;
            }
        }
    }

    /**
     * onClick method for list
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(dbManager.getDateFromDatabase(id));
        dialog.setMessage(dbManager.getSubjectFromDatabase(id) + "\n" + dbManager.getBodyFromDatabase(id));
        dialog.create();

        dialog.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void loadNewNote(View view) {
        startActivity(new Intent(getApplicationContext(), NewNoteActivity.class));
    }

    public void shareNotesActivity(View view) {
        startActivity(new Intent(getApplicationContext(), ShareNotesActivity.class));
    }

    public void mainActivity (View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; adds items to the action bar if it is present
        getMenuInflater( ).inflate( R.menu.menu_view_and_search, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                mainActivity(null);
                return true;
            case R.id.menu_share_notes:
                shareNotesActivity(null);
                return true;
        }
        return false;
    }

}
