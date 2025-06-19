package com.cyberpokemon.plancraft;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TASK= "tasks";

    private static final String KEY_ID="id";
    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_DEADLINE="deadline";
    private static final String KEY_IS_COMPLETED="is_completed";
    private static final String KEY_REMINDER_BEFORE="reminder_before";
    private static final String KEY_FOLLOW_UP_FREQUENCY="follow_up_frequency";
    private static final String KEY_DEADLINE_CROSSED_FREQUENCY="deadline_crossed_frequency";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
