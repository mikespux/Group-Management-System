package com.gms.activities;

import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gms.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.format.DateTimeFormatter;


/**
 * Shows off the most basic usage
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity
    implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");


  MaterialCalendarView widget;

  TextView textView;
  public Toolbar toolbar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);
    setupToolbar();

    widget=findViewById(R.id.calendarView);
    textView=findViewById(R.id.textView);
    widget.setOnDateChangedListener(this);
   widget.setOnDateLongClickListener(this);
    widget.setOnMonthChangedListener(this);

    //Setup initial text
    textView.setText("No Selection");
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
}
