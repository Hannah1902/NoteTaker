package com.example.hannah.notetaker;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    DatabaseManager dbManager;
    NoteTaker nt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nt = new NoteTaker();

        dbManager = new DatabaseManager(this);
        try {
            SQLiteDatabase db = dbManager.getWritableDatabase();
        } catch (SQLException se) {
            Toast.makeText(this, se.getMessage(), Toast.LENGTH_LONG).show();
        }
        /* This is not is use

        ArrayAdapter dateAdapter = dbManager.fillAutoCompleteTextFields(this, DatabaseManager.DATE);
        if (dateAdapter != null) {
            AutoCompleteTextView dateEntry = (AutoCompleteTextView) findViewById(R.id.date_entry);
            dateEntry.setAdapter(dateAdapter);
        }


        ArrayAdapter subjectAdapter = dbManager.fillAutoCompleteTextFields(this, DatabaseManager.SUBJECT);
        if (subjectAdapter != null) {
            AutoCompleteTextView subjectEntry = (AutoCompleteTextView) findViewById(R.id.subject_search);
            subjectEntry.setAdapter(subjectAdapter);
        }
        */
    }

    public void loadNewNote(View view) {
        startActivity(new Intent(getApplicationContext(), NewNoteActivity.class));
    }

    public void displayNotesActivity (View view) {
        startActivity(new Intent(getApplicationContext(), ViewNotesActivity.class));
    }

    public void shareNotesActivity(View view) {
        startActivity(new Intent(getApplicationContext(), ShareNotesActivity.class));
    }


    public void displayAllNotes(View view) {
        ToggleButton hideNotes = (ToggleButton)  findViewById(R.id.display_notes);
        TextView noteDisplay = (TextView) findViewById(R.id.notes_content);

        String notes = "";

        ArrayList<String> allNotes = dbManager.selectAll();

        for (String s : allNotes) {
            notes += s + "\n";
        }

        noteDisplay.setText(notes);

        if (hideNotes.isChecked()) {
            noteDisplay.setVisibility(TextView.INVISIBLE);
        }
        else {
            noteDisplay.setVisibility(TextView.VISIBLE);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; adds items to the action bar if it is present
        getMenuInflater( ).inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_all_notes:
                displayNotesActivity(null);
                return true;
            case R.id.menu_share_notes:
                shareNotesActivity(null);
                return true;
        }
        return false;
    }


}
