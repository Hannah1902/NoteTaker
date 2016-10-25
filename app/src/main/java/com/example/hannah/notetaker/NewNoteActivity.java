package com.example.hannah.notetaker;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class NewNoteActivity extends AppCompatActivity {

    DatabaseManager dbManager;
    NoteTaker nt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        dbManager = new DatabaseManager(this);
        try {
            SQLiteDatabase dp = dbManager.getWritableDatabase();
        }
        catch (SQLException se) {
            Toast.makeText(this, se.getMessage(), Toast.LENGTH_LONG).show();
        }

        nt = new NoteTaker();
    }

    /**
     * Saves notes to a database
     * @param view
     */
    public void saveNote (View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        EditText noteBodyET = (EditText) findViewById(R.id.text);
        String noteBody = noteBodyET.getText().toString();

        EditText noteSubjectET = (EditText) findViewById(R.id.note_subject);
        String subject = noteSubjectET.getText().toString();

        nt.setNoteSubject(subject);

        if (nt.setNoteBody(noteBody)) {
            //Insert if character limit is met
            Toast.makeText(this, nt.lengthMessage(noteBody), Toast.LENGTH_LONG).show();
            dbManager.insertNote(nt.getDate(), nt.getNoteSubject(), nt.getNoteBody());
        }
        else {
            //Inform user that text is over limit. Do not insert note
            Toast.makeText(this, nt.lengthMessage(noteBody), Toast.LENGTH_LONG).show();
        }
    }

}
