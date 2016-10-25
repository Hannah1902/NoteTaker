package com.example.hannah.notetaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Hannah on 10/16/16.
 *
 */
public class ActvityHelper extends AppCompatActivity{

    public ActvityHelper () {

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
