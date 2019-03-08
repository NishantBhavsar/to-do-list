package com.nishantbhavsar.doittodolist;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.DatePickerDialog;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class add_task extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    EditText edittextDate;
    DatePickerDialog.OnDateSetListener date;
    EditText edittextTime;
    EditText edittextCategory;
    EditText task;
    EditText taskDescription;
    CheckBox taskDoneOrNot;
    String completed = "no";
    Task taskClass;
    public DatabaseHandler dbHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHandle = DatabaseHandler.getInstance(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        getSupportActionBar().setTitle("New Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edittextDate= (EditText) findViewById(R.id.due_date);
        edittextTime= (EditText) findViewById(R.id.due_time);
        edittextCategory = (EditText) findViewById(R.id.category);
        task = (EditText) findViewById(R.id.task);
        taskDescription = (EditText) findViewById(R.id.task_description);
        taskDoneOrNot = (CheckBox) findViewById(R.id.task_done_or_not);

        if(title.equals("To Do Lists") || title.equals("Today") || title.equals("Completed") || title.equals("Uncategorized")){
            edittextCategory.setText("Uncategorized");
        }
        else{
            edittextCategory.setText(title);
        }

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate();
            }
        };

        edittextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(add_task.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edittextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(add_task.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);
                        myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute);

                        String am_pm = "";
                        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";

                        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
                        int showMunutes = datetime.get(Calendar.MINUTE);
                        String showMinStr = String.valueOf(showMunutes);
                        if(showMunutes < 10){
                            showMinStr = "0"+ showMinStr;
                        }
                        edittextTime.setText( strHrsToShow+":"+showMinStr+" "+am_pm);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        taskDoneOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    completed = "yes";
                }
                else{
                    task.setPaintFlags(task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    completed = "no";
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_task) {
            if(task.getText().toString().equals("")){
                Toast.makeText(this, "Enter task at first", Toast.LENGTH_SHORT).show();
            }
            else{
                taskClass = new Task();
                taskClass.set_task(task.getText().toString());
                taskClass.set_status(completed);
                taskClass.set_category(edittextCategory.getText().toString());
                taskClass.set_date(edittextDate.getText().toString());
                taskClass.set_time(edittextTime.getText().toString());
                taskClass.set_description(taskDescription.getText().toString());

                long rowInserted = dbHandle.addTask(taskClass);
                if(rowInserted != -1) {
                    //Toast.makeText(this, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "New task added" , Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();

                Calendar tempCalender = Calendar.getInstance();
                long seconds = (myCalendar.getTimeInMillis() - tempCalender.getTimeInMillis()) - 5000;
                if(seconds > 0) {
                    scheduleNotification(getNotification(taskClass.get_task(), taskClass.get_category()), seconds);
                    Toast.makeText(this, "Reminder is Set", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(add_task.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateLabelDate() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edittextDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void clearDueDate(View view){
        edittextDate.setText("");
    }

    public void clearDueTime(View view){
        edittextTime.setText("");
    }


    private void scheduleNotification(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content, String category) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(content);
        builder.setContentText(category);
        builder.setSmallIcon(R.drawable.ic_all_24dp);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        return builder.build();
    }
}
