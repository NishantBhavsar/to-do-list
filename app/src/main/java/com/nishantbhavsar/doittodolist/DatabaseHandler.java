package com.nishantbhavsar.doittodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 11/8/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler sInstance;

    public static synchronized DatabaseHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasksDatabase";

    // Contacts table name
    private static final String TABLE_TASKS = "tasksTable";


    //File dbFile;
    //private Context mContext;

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TASK = "task";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATE = "duedate";
    private static final String KEY_TIME = "duetime";
    private static final String KEY_DESCRIPTION = "description";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("database onCreate ");
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_TASK + " TEXT," + KEY_STATUS + " TEXT," + KEY_CATEGORY + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_DESCRIPTION + " TEXT" +  ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }

    /*
    public boolean checkDataBase() {
        //dbFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"PhotoApp");
        //return dbFile.exists();
        //return mContext.getDatabasePath(DATABASE_NAME).exists();
    }
    */

    // Adding new contact
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK, task.get_task());
        values.put(KEY_STATUS, task.get_status());
        values.put(KEY_CATEGORY, task.get_category());
        values.put(KEY_DATE, task.get_date());
        values.put(KEY_TIME, task.get_time());
        values.put(KEY_DESCRIPTION, task.get_description());

        // Inserting Row
        long rowInserted = db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
        return rowInserted;
    }

    // Getting single contact
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID, KEY_TASK, KEY_STATUS, KEY_CATEGORY, KEY_DATE, KEY_TIME, KEY_DESCRIPTION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task();
        task.set_id(Integer.parseInt(cursor.getString(0)));
        task.set_task(cursor.getString(1));
        task.set_status(cursor.getString(2));
        task.set_category(cursor.getString(3));
        task.set_date(cursor.getString(4));
        task.set_time(cursor.getString(5));
        task.set_description(cursor.getString(6));

        // return contact
        return task;
    }

    // Getting All Tasks
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.set_id(Integer.parseInt(cursor.getString(0)));
                task.set_task(cursor.getString(1));
                task.set_status(cursor.getString(2));
                task.set_category(cursor.getString(3));
                task.set_date(cursor.getString(4));
                task.set_time(cursor.getString(5));
                task.set_description(cursor.getString(6));
                // Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return taskList;
    }

    public ArrayList<Task> getTodayTasks() {
        Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String today = sdf.format(myCalendar.getTime());

        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_DATE + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ today });

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.set_id(Integer.parseInt(cursor.getString(0)));
                task.set_task(cursor.getString(1));
                task.set_status(cursor.getString(2));
                task.set_category(cursor.getString(3));
                task.set_date(cursor.getString(4));
                task.set_time(cursor.getString(5));
                task.set_description(cursor.getString(6));
                // Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return taskList;
    }


    public ArrayList<Task> getStatusTasks(String status) {

        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS + " =  ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { status });

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.set_id(Integer.parseInt(cursor.getString(0)));
                task.set_task(cursor.getString(1));
                task.set_status(cursor.getString(2));
                task.set_category(cursor.getString(3));
                task.set_date(cursor.getString(4));
                task.set_time(cursor.getString(5));
                task.set_description(cursor.getString(6));
                // Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return taskList;
    }

    public ArrayList<Task> getCategoryTasks(String category) {

        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_CATEGORY + " =  ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { category });

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.set_id(Integer.parseInt(cursor.getString(0)));
                task.set_task(cursor.getString(1));
                task.set_status(cursor.getString(2));
                task.set_category(cursor.getString(3));
                task.set_date(cursor.getString(4));
                task.set_time(cursor.getString(5));
                task.set_description(cursor.getString(6));
                // Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return taskList;
    }



    // Updating single contact
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, task.get_status());
        // updating row
        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.get_id()) });
    }

}
