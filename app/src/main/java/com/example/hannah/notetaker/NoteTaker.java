package com.example.hannah.notetaker;

import java.util.Calendar;

/**
 * Created by Hannah on 10/3/16.
 * NoteTaker object class providing implementation for basic methods regarding accessing
 * and mutating note data
 */
public class NoteTaker {

    public final int MAX_LENGTH = 125;
    private String noteBody;
    private String noteSubject;
    private String date;

    public NoteTaker() {
        noteBody = "";
        noteSubject = "";

    }

    public String lengthMessage(String body) {
        if (body.length() > MAX_LENGTH) {
            return "The character limit is " + MAX_LENGTH + ". You are " + (MAX_LENGTH - body.length())
                    + " characters over the limit.";
        }
        else {
            return "Character count: " + body.length();
        }
    }

    public String getNoteBody() {
        return noteBody;
    }

    public String getNoteSubject() {
        return noteSubject;
    }

    public String getDate() {
        Calendar now = Calendar.getInstance();
        date = (now.get(Calendar.MONTH) + 1 + "/"
                + now.get(Calendar.DATE) + "/"
                + now.get(Calendar.YEAR));
        return date;
    }

    public int getNoteLength (String body) {
        return body.length();
    }

    public boolean setNoteBody (String body) {
        if (getNoteLength(body) > MAX_LENGTH) {
            return false;
        }
        else {
            this.noteBody = body;
            return true;
        }
    }

    public void setNoteDate (String date) { this.date = date; }

    public void setNoteSubject(String subject) {
        this.noteSubject = subject;
    }
}

