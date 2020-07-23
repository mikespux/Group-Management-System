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
public class ContributionActivity extends AppCompatActivity {

  public Toolbar toolbar;

  String CurrentDate;


  TextView textView,add_contribution;

  DBHelper dbhelper;
  EditText min_contribution_name;
  String s_min_contribution_name,s_min_date,s_min_time;

  FloatingActionButton float_addContribution;
  TextView textAccountId;
  String accountId;
  ListView lvContribution;

  Boolean success = true;
  AlertDialog dContribution;
  Intent mIntent;
  SharedPreferences prefs ;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contribution);
    setupToolbar();
    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    textView=findViewById(R.id.textView);

    add_contribution=findViewById(R.id.add_contribution);
    add_contribution.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showAddContributionDialog();
      }
    });


    //Setup initial text
   // textView.setText(prefs.getString("textView", ""));
    CurrentDate = prefs.getString("CurrentDate", "");

    initializer();
  }
  public void initializer(){
    dbhelper = new DBHelper(getApplicationContext());



    lvContribution=this.findViewById(R.id.lvContribution);
    lvContribution.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View selectedView, int arg2, long arg3) {
        textAccountId = selectedView.findViewById(R.id.txtAccountId);
        Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());

        mIntent = new Intent(getApplicationContext(), ContributeActivity.class);
        startActivity(mIntent);
        //showUpdateElectionDialog();
      }
    });
    lvContribution.setLongClickable(true);
    lvContribution.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> view, View arg1,
                                     int pos, long id) {
        textAccountId = view.findViewById(R.id.txtAccountId);
        Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());
        // TODO Auto-generated method stub
        showUpdateElectionDialog();
        Log.v("long clicked","pos: " + pos);


        Toast.makeText(getApplicationContext(), textAccountId.getText().toString(), Toast.LENGTH_LONG).show();
        return true;
      }

    });

  }
  public void setupToolbar() {
    toolbar = findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Contributions Towards");

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  public void showAddContributionDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.dialog_contibution_new, null);
    dialogBuilder.setView(dialogView);
    // dialogBuilder.setTitle("New Election");

    toolbar = dialogView.findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("New");

    min_contribution_name=dialogView.findViewById(R.id.min_contribution_name);
    float_addContribution=dialogView.findViewById(R.id.float_addcontribution);


    float_addContribution.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        try {
          s_min_contribution_name = min_contribution_name.getText().toString();
          Calendar cal = Calendar.getInstance();
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
          SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          s_min_date =format.format(cal.getTime());
          s_min_time =format1.format(cal.getTime());
          if (s_min_contribution_name.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter contribution name for the Meeting", Toast.LENGTH_LONG).show();
            return;
          }

          dbhelper.AddContributions(s_min_contribution_name,s_min_date, s_min_time);
          if (success) {


            Toast.makeText(getApplicationContext(), "Saved successfully!!", Toast.LENGTH_LONG).show();

            min_contribution_name.setText("");

            getdata();
            dContribution.dismiss();
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
    dContribution = dialogBuilder.create();
    dContribution.show();
  }

  public void showUpdateElectionDialog() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.dialog_contibution_new, null);
    dialogBuilder.setView(dialogView);
    // dialogBuilder.setTitle("Update Election");

    toolbar = dialogView.findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Update");

    accountId = textAccountId.getText().toString();

    min_contribution_name = dialogView.findViewById(R.id.min_contribution_name);


    dbhelper = new DBHelper(this);
    SQLiteDatabase db = dbhelper.getReadableDatabase();
    Cursor account = db.query(Database.CONTRIBUTIONS_TABLE_NAME, null,
            " _id = ?", new String[] { accountId }, null, null, null);
    //startManagingCursor(accounts);
    if (account.moveToFirst()) {
      // update view
      min_contribution_name.setText(account.getString(account
              .getColumnIndex(Database.CON_NAME)));




    }
    account.close();
    db.close();
    dbhelper.close();



    float_addContribution = dialogView.findViewById(R.id.float_addcontribution);
    float_addContribution.setVisibility(View.GONE);


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
      values.put( Database.CON_NAME, min_contribution_name.getText().toString());



      long rows = db.update(Database.CONTRIBUTIONS_TABLE_NAME, values,
              "_id = ?", new String[] { accountId });

      db.close();
      if (rows > 0){
        Toast.makeText(this, "Updated Successfully!",
                Toast.LENGTH_LONG).show();
      }
      else{
        Toast.makeText(this, "Sorry! Could not update!",
                Toast.LENGTH_LONG).show();}
    } catch (Exception ex) {
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  public void deleteElection() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to delete?")
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
        Toast.makeText(this, "Could not delete!", Toast.LENGTH_LONG).show();}
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
      Cursor accounts = db.query( true, Database.CONTRIBUTIONS_TABLE_NAME,null,Database.ROW_ID + ">'" + ROWID + "'",null,null,null,null,null,null);

      String from [] = {  Database.ROW_ID,Database.CON_NAME , Database.CON_DATE};
      int to [] = { R.id.txtAccountId,R.id.txtMinute,R.id.txtTime};

      SimpleCursorAdapter ca  = new SimpleCursorAdapter(this,R.layout.con_item, accounts,from,to);
      lvContribution=this.findViewById(R.id.lvContribution);
      lvContribution.setAdapter(ca);
      dbhelper.close();
    } catch (Exception ex) {
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

}
