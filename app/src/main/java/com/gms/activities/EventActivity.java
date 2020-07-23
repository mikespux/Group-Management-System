package com.gms.activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gms.R;
import com.gms.database.DBHelper;
import com.gms.database.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


/**
 * Shows off the most basic usage
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class EventActivity extends AppCompatActivity {

  public Toolbar toolbar;

  String CurrentDate;


  TextView textView,add_event;

  DBHelper dbhelper;
  EditText min_event_name;
  String s_min_event_name,s_min_date,s_min_time;

  FloatingActionButton float_addevent;
  TextView textAccountId;
  String accountId;
  ListView listEvents;

  Boolean success = true;
  AlertDialog dEvent;
  Intent mIntent;
  SharedPreferences prefs ;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event);
    setupToolbar();
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    textView=findViewById(R.id.textView);

    add_event=findViewById(R.id.add_event);
    add_event.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showAddEventDialog();
      }
    });


    //Setup initial text
    textView.setText(prefs.getString("textView", ""));
    CurrentDate = prefs.getString("CurrentDate", "");

    try {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      Date dateToday = new Date();
      String today = sdf.format(dateToday);

      if (new SimpleDateFormat("dd/MM/yyyy").parse(CurrentDate).before(new Date())) {

        add_event.setVisibility(View.GONE);
        if(CurrentDate.equals(today)){
          add_event.setVisibility(View.VISIBLE);
        }
      }

    } catch (ParseException e) {
      e.printStackTrace();
    }

    initializer();
  }
  public void initializer(){
    dbhelper = new DBHelper(getApplicationContext());



    listEvents=this.findViewById(R.id.lvEvents);
    listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View selectedView, int arg2, long arg3) {
        textAccountId = selectedView.findViewById(R.id.txtAccountId);
        Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());
        showUpdateEventDialog();
      }
    });

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

  public void showAddEventDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.dialog_event_new, null);
    dialogBuilder.setView(dialogView);
    // dialogBuilder.setTitle("New Event");

    toolbar = dialogView.findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("New Event");

    min_event_name=dialogView.findViewById(R.id.min_event_name);
    float_addevent=dialogView.findViewById(R.id.float_addevent);


    float_addevent.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        try {
          s_min_event_name = min_event_name.getText().toString();
          Calendar cal = Calendar.getInstance();
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
          SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
         // s_min_date =format.format(cal.getTime());
          s_min_date =CurrentDate;
          s_min_time =format1.format(cal.getTime());
          if (s_min_event_name.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter event_name for the Meeting", Toast.LENGTH_LONG).show();
            return;
          }

          dbhelper.AddEvent(s_min_event_name,s_min_date, s_min_time);
          if (success) {


            Toast.makeText(getApplicationContext(), "Saved successfully!!", Toast.LENGTH_LONG).show();

            min_event_name.setText("");

            getdata();
            dEvent.dismiss();
          }
        } catch (Exception e) {
          success = false;

          if (success) {
            Toast.makeText(getApplicationContext(), "Saving  Failed", Toast.LENGTH_LONG).show();
          }

        }

      }
    });

    dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        //do something with edt.getText().toString();
        getdata();

      }
    });
        /*
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                getdata();
            }
        });*/
    dEvent = dialogBuilder.create();
    dEvent.show();
  }

  public void showUpdateEventDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.dialog_event_new, null);
    dialogBuilder.setView(dialogView);
    // dialogBuilder.setTitle("Update Event");

    toolbar = dialogView.findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Update Event");

    accountId = textAccountId.getText().toString();

    min_event_name = dialogView.findViewById(R.id.min_event_name);


    dbhelper = new DBHelper(this);
    SQLiteDatabase db = dbhelper.getReadableDatabase();
    Cursor account = db.query(Database.EVENT_TABLE_NAME, null,
            " _id = ?", new String[] { accountId }, null, null, null);
    //startManagingCursor(accounts);
    if (account.moveToFirst()) {
      // update view
      min_event_name.setText(account.getString(account
              .getColumnIndex(Database.EVENT_NAME)));




    }
    account.close();
    db.close();
    dbhelper.close();



    float_addevent = dialogView.findViewById(R.id.float_addevent);
    float_addevent.setVisibility(View.GONE);


    dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        //do something with edt.getText().toString();
        deleteEvent();

      }
    });
    dialogBuilder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        //pass
        updateEvent();
        getdata();



      }
    });
    AlertDialog b = dialogBuilder.create();
    b.show();
  }

  public void updateEvent() {
    try {
      dbhelper = new DBHelper(this);
      SQLiteDatabase db = dbhelper.getWritableDatabase();
      // execute insert command

      ContentValues values = new ContentValues();
      values.put( Database.EVENT_NAME, min_event_name.getText().toString());



      long rows = db.update(Database.EVENT_TABLE_NAME, values,
              "_id = ?", new String[] { accountId });

      db.close();
      if (rows > 0){
        Toast.makeText(this, "Updated Event Successfully!",
                Toast.LENGTH_LONG).show();
      }
      else{
        Toast.makeText(this, "Sorry! Could not update Event!",
                Toast.LENGTH_LONG).show();}
    } catch (Exception ex) {
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  public void deleteEvent() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to delete this Event?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                deleteCurrentAccount();

              }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
              }
            });
    AlertDialog alert = builder.create();
    alert.show();
  }



  public void deleteCurrentAccount() {
    try {
      DBHelper dbhelper = new DBHelper(this);
      SQLiteDatabase db = dbhelper.getWritableDatabase();
      int rows = db.delete(Database.EVENT_TABLE_NAME, "_id=?", new String[]{ accountId});
      dbhelper.close();
      if ( rows == 1) {
        Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_LONG).show();

        //this.finish();
        getdata();
      }
      else{
        Toast.makeText(this, "Could not delete Event!", Toast.LENGTH_LONG).show();}
      //}

    } catch (Exception ex) {
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }

  }




  public void onStart() {
    super.onStart();
    getdata();
  }


  public void getdata(){

    try {

      SQLiteDatabase db= dbhelper.getReadableDatabase();
      Cursor accounts = db.query( true, Database.EVENT_TABLE_NAME,null,Database.EVENT_DATE + "='" + CurrentDate + "'",null,null,null,null,null,null);

      String from [] = {  Database.ROW_ID,Database.EVENT_NAME , Database.EVENT_DATE};
      int to [] = { R.id.txtAccountId,R.id.txtMinute,R.id.txtTime};

      SimpleCursorAdapter ca  = new SimpleCursorAdapter(this,R.layout.min_item, accounts,from,to);
      listEvents=this.findViewById(R.id.lvEvents);
      listEvents.setAdapter(ca);
      dbhelper.close();
    } catch (Exception ex) {
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

}
