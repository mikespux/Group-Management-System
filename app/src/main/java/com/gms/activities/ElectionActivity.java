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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Shows off the most basic usage
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ElectionActivity extends AppCompatActivity {

  public Toolbar toolbar;

  String CurrentDate;


  TextView textView,add_election;

  DBHelper dbhelper;
  EditText min_election_name;
  String s_min_election_name,s_min_date,s_min_time;

  FloatingActionButton float_addelection;
  TextView textAccountId;
  String accountId;
  ListView listElections;

  Boolean success = true;
  AlertDialog dElection;
  Intent mIntent;
  SharedPreferences prefs;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_election);
    setupToolbar();
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    textView=findViewById(R.id.textView);

    add_election=findViewById(R.id.add_election);
    add_election.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showAddElectionDialog();
      }
    });


    //Setup initial text
   // textView.setText(prefs.getString("textView", ""));
    CurrentDate = prefs.getString("CurrentDate", "");

    initializer();
  }
  public void initializer(){
    dbhelper = new DBHelper(getApplicationContext());



    listElections=this.findViewById(R.id.lvElections);
    listElections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View selectedView, int arg2, long arg3) {
        textAccountId = selectedView.findViewById(R.id.txtAccountId);
        Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());
        showUpdateElectionDialog();
      }
    });

  }
  public void setupToolbar() {
    toolbar = findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.menu_voting);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  public void showAddElectionDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.dialog_election_new, null);
    dialogBuilder.setView(dialogView);
    // dialogBuilder.setTitle("New Election");

    toolbar = dialogView.findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("New Election");

    min_election_name=dialogView.findViewById(R.id.min_election_name);
    float_addelection=dialogView.findViewById(R.id.float_addelection);


    float_addelection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        try {
          s_min_election_name = min_election_name.getText().toString();
          Calendar cal = Calendar.getInstance();
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
          SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          s_min_date =format.format(cal.getTime());
          s_min_time =format1.format(cal.getTime());
          if (s_min_election_name.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter ELECTION_NAME for the Meeting", Toast.LENGTH_LONG).show();
            return;
          }

          dbhelper.AddElection(s_min_election_name,s_min_date, s_min_time);
          if (success) {


            Toast.makeText(getApplicationContext(), "Saved successfully!!", Toast.LENGTH_LONG).show();

            min_election_name.setText("");

            getdata();
            dElection.dismiss();
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
    dElection = dialogBuilder.create();
    dElection.show();
  }

  public void showUpdateElectionDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.dialog_election_new, null);
    dialogBuilder.setView(dialogView);
    // dialogBuilder.setTitle("Update Election");

    toolbar = dialogView.findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Update Election");

    accountId = textAccountId.getText().toString();

    min_election_name = dialogView.findViewById(R.id.min_election_name);


    dbhelper = new DBHelper(this);
    SQLiteDatabase db = dbhelper.getReadableDatabase();
    Cursor account = db.query(Database.ELECTION_TABLE_NAME, null,
            " _id = ?", new String[] { accountId }, null, null, null);
    //startManagingCursor(accounts);
    if (account.moveToFirst()) {
      // update view
      min_election_name.setText(account.getString(account
              .getColumnIndex(Database.ELECTION_NAME)));




    }
    account.close();
    db.close();
    dbhelper.close();



    float_addelection = dialogView.findViewById(R.id.float_addelection);
    float_addelection.setVisibility(View.GONE);


    dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        //do something with edt.getText().toString();
        deleteElection();

      }
    });
    dialogBuilder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        //pass
        updateElection();
        getdata();



      }
    });
    AlertDialog b = dialogBuilder.create();
    b.show();
  }

  public void updateElection() {
    try {
      dbhelper = new DBHelper(this);
      SQLiteDatabase db = dbhelper.getWritableDatabase();
      // execute insert command

      ContentValues values = new ContentValues();
      values.put( Database.ELECTION_NAME, min_election_name.getText().toString());



      long rows = db.update(Database.ELECTION_TABLE_NAME, values,
              "_id = ?", new String[] { accountId });

      db.close();
      if (rows > 0){
        Toast.makeText(this, "Updated Election Successfully!",
                Toast.LENGTH_LONG).show();
      }
      else{
        Toast.makeText(this, "Sorry! Could not update Election!",
                Toast.LENGTH_LONG).show();}
    } catch (Exception ex) {
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  public void deleteElection() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to delete this Election?")
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
      int rows = db.delete(Database.ELECTION_TABLE_NAME, "_id=?", new String[]{ accountId});
      dbhelper.close();
      if ( rows == 1) {
        Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_LONG).show();

        //this.finish();
        getdata();
      }
      else{
        Toast.makeText(this, "Could not delete Election!", Toast.LENGTH_LONG).show();}
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
    int ROWID=0;
      SQLiteDatabase db= dbhelper.getReadableDatabase();
      Cursor accounts = db.query( true, Database.ELECTION_TABLE_NAME,null,Database.ROW_ID + ">'" + ROWID + "'",null,null,null,null,null,null);

      String from [] = {  Database.ROW_ID,Database.ELECTION_NAME , Database.ELECTION_DATE};
      int to [] = { R.id.txtAccountId,R.id.txtMinute,R.id.txtTime};

      SimpleCursorAdapter ca  = new SimpleCursorAdapter(this,R.layout.min_item, accounts,from,to);
      listElections=this.findViewById(R.id.lvElections);
      listElections.setAdapter(ca);
      dbhelper.close();
    } catch (Exception ex) {
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

}
