package com.gms.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gms.R;
import com.gms.database.DBHelper;
import com.gms.database.Database;
import com.gms.decorators.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;


/**
 * Shows off the most basic usage
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity
    implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
  private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  String CurrentDate;
  MaterialCalendarView widget;

  TextView textView;
  public Toolbar toolbar;

  Intent mIntent;
  SharedPreferences prefs;

  DBHelper dbhelper;
  SQLiteDatabase db;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);
    dbhelper = new DBHelper(getApplicationContext());
    db=dbhelper.getReadableDatabase();
    setupToolbar();
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    widget = findViewById(R.id.calendarView);
    textView = findViewById(R.id.textView);

    widget.setOnDateChangedListener(this);
    widget.setOnDateLongClickListener(this);
    widget.setOnMonthChangedListener(this);


    new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    //Setup initial text
    textView.setText("No Selection");
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    CurrentDate = format.format(cal.getTime());
    SharedPreferences prefs = PreferenceManager
            .getDefaultSharedPreferences(CalendarActivity.this);


  }

  @Override
  protected void onResume() {
    super.onResume();
    new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
  }

  public void setupToolbar() {
    toolbar = findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.menu_calendar);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  @Override
  public void onDateSelected(
          @NonNull MaterialCalendarView widget,
          @NonNull CalendarDay date,
          boolean selected) {
    textView.setText(selected ? FORMATTER.format(date.getDate()) : "No Selection");
    CurrentDate = FORMAT.format(date.getDate());
    SharedPreferences.Editor edit = prefs.edit();
    edit.putString("CurrentDate", CurrentDate);
    edit.commit();

    edit.putString("textView", textView.getText().toString());
    edit.commit();
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      Date dateToday = new Date();
      String today = sdf.format(dateToday);
      if (new SimpleDateFormat("dd/MM/yyyy").parse(CurrentDate).before(new Date())) {
        if(CurrentDate.equals(today)){
          mIntent = new Intent(getApplicationContext(), EventActivity.class);
          startActivity(mIntent);
          return;
        }
        pastEvent();

      }else{
        mIntent = new Intent(getApplicationContext(), EventActivity.class);
        startActivity(mIntent);

      }
    } catch (ParseException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
    final String text = String.format("%s is available", FORMATTER.format(date.getDate()));
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
    //noinspection ConstantConditions
    getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
  }
  public void pastEvent() {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
            CalendarActivity.this);
    // Setting Dialog Title
    alertDialog.setTitle("Past Event");
    // Setting Dialog Message
    alertDialog.setMessage("View Past Events?");

    // Setting Positive "Yes" Button
    alertDialog.setNegativeButton("YES",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                mIntent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(mIntent);


              }
            });
    // Setting Negative "NO" Button
    alertDialog.setPositiveButton("NO",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
              }
            });
    // Showing Alert Message
    alertDialog.show();
  }
  /**
   * Simulate an API call to show how to add decorators
   */
  private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

    @Override
    protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      LocalDate eventTime;
      final ArrayList<CalendarDay> dates = new ArrayList<>();
      String selectQuery = "SELECT * FROM " + Database.EVENT_TABLE_NAME + "";
      Cursor cursor = db.rawQuery(selectQuery, null);
if (cursor.getCount()>0){
      if (cursor.moveToFirst()) {
        do {
          eventTime=LocalDate.parse(cursor.getString(cursor
                  .getColumnIndex(Database.EVENT_DATE)),formatter);
          final CalendarDay day = CalendarDay.from(eventTime);
          dates.add(day);

        } while (cursor.moveToNext());
      }
     }
      return dates;
    }

    @Override
    protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
      super.onPostExecute(calendarDays);

      if (isFinishing()) {
        return;
      }

      widget.addDecorator(new EventDecorator(Color.RED, calendarDays));
    }
  }
}