package com.cyberpokemon.plancraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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
    private static final String KEY_COMPLETED_TIME="completed_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DEADLINE + " INTEGER,"
                + KEY_IS_COMPLETED + " INTEGER,"
                + KEY_REMINDER_BEFORE + " INTEGER,"
                + KEY_FOLLOW_UP_FREQUENCY + " INTEGER,"
                + KEY_DEADLINE_CROSSED_FREQUENCY + " INTEGER,"
                + KEY_COMPLETED_TIME+" INTEGER"
                + ")";
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        onCreate(db);
    }

    public long addTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_DEADLINE, task.getDeadlineMillis());
        values.put(KEY_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(KEY_REMINDER_BEFORE, task.getReminderBeforeMillis());
        values.put(KEY_FOLLOW_UP_FREQUENCY, task.getFollowUpFrequencyMillis());
        values.put(KEY_DEADLINE_CROSSED_FREQUENCY,task.getDeadlineCrossedMillis());
        values.put(KEY_COMPLETED_TIME,task.getCompletedTimeMillis());

        long result=db.insert(TABLE_TASK,null,values);
        db.close();
        return result;
    }


    public List<Task> getAllTask()
    {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASK,null,null,null,null,null,KEY_DEADLINE+" ASC");

        if(cursor.moveToFirst())
        {
            do
            {
                Task task = new Task(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DEADLINE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_COMPLETED))==1,
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_REMINDER_BEFORE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_FOLLOW_UP_FREQUENCY)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DEADLINE_CROSSED_FREQUENCY))
                );

                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));

                taskList.add(task);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    public int updateTask(int id, Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_DEADLINE, task.getDeadlineMillis());
        values.put(KEY_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(KEY_REMINDER_BEFORE, task.getReminderBeforeMillis());
        values.put(KEY_FOLLOW_UP_FREQUENCY, task.getFollowUpFrequencyMillis());
        values.put(KEY_DEADLINE_CROSSED_FREQUENCY,task.getDeadlineCrossedMillis());

        int rows = db.update(TABLE_TASK,values,KEY_ID+"=?",new String[]{String.valueOf(id)});

        db.close();

        return rows;
    }

    public int deleteTask(int  id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int rows = db.delete(TABLE_TASK,KEY_ID+"=?",new String[]{String.valueOf((id))});
        db.close();
        return rows;
    }

    public List<Task> getAllIncompleteTasks()
    {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASK,null,KEY_IS_COMPLETED+" =?",new String[]{"0"},null,null,KEY_DEADLINE+ " ASC");

        if(cursor.moveToFirst())
        {
            do
            {
                Task task = new Task(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DEADLINE)),
                        false,
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_REMINDER_BEFORE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_FOLLOW_UP_FREQUENCY)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DEADLINE_CROSSED_FREQUENCY))

                );
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));

                taskList.add(task);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    public List<Task> getAllCompleteTasks()
    {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASK,null,KEY_IS_COMPLETED+" =?",new String[]{"1"},null,null,KEY_DEADLINE+ " ASC");

        if(cursor.moveToFirst())
        {
            do
            {
                Task task = new Task(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DEADLINE)),
                        true,
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_REMINDER_BEFORE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_FOLLOW_UP_FREQUENCY)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DEADLINE_CROSSED_FREQUENCY))

                );

                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                task.setCompletedTimeMillis(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_COMPLETED_TIME)));

                taskList.add(task);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }


    public int markTaskAsComplete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_COMPLETED, 1);
        values.put(KEY_COMPLETED_TIME, System.currentTimeMillis());

        int rows=db.update(TABLE_TASK, values, KEY_ID + "=?", new String[]{String.valueOf(id)});

        db.close();
        return rows;
    }
}
