package com.gms.activities;

import android.annotation.TargetApi;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gms.R;
import com.gms.database.DBHelper;
import com.gms.database.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MinutesActivity extends AppCompatActivity {
    public Toolbar toolbar;
    DBHelper dbhelper;
    EditText min_agenda;
    String s_min_agenda,s_min_date,s_min_time;
    LinearLayout LtNew,LtPrevious,LtSearch;

    FloatingActionButton float_addminute;
    TextView textAccountId;
    String accountId;
    ListView listMinutes;

    Boolean success = true;
    AlertDialog dMinute;
    Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minutes);
        setupToolbar();
        initializer();
    }

    public void setupToolbar() {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.menu_minutes);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void initializer(){
        dbhelper = new DBHelper(getApplicationContext());

        LtNew=findViewById(R.id.ltNew);
        LtNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddMinuteDialog();
            }
        });
        LtPrevious=findViewById(R.id.ltPrevious);
        LtSearch=findViewById(R.id.ltSearch);

        LtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent= new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(mIntent);
            }
        });




    }

    public void showAddMinuteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_minutes_new, null);
        dialogBuilder.setView(dialogView);
       // dialogBuilder.setTitle("New Minute");

        toolbar = dialogView.findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Minute");

        min_agenda=dialogView.findViewById(R.id.min_agenda);
        float_addminute=dialogView.findViewById(R.id.float_addminute);


        float_addminute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    s_min_agenda = min_agenda.getText().toString();
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    s_min_date =format.format(cal.getTime());
                    s_min_time =format1.format(cal.getTime());
                    if (s_min_agenda.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter Agenda for the Meeting", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mIntent= new Intent(getApplicationContext(), NotesActivity.class);
                    startActivity(mIntent);
                  //  dbhelper.AddMinute(s_min_agenda,s_min_date, s_min_time);
                    if (success) {
                        SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(MinutesActivity.this);

                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("agenda", s_min_agenda);
                        edit.commit();


                        ///Toast.makeText(getApplicationContext(), "Saved successfully!!", Toast.LENGTH_LONG).show();

                        min_agenda.setText("");


                        dMinute.dismiss();
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


            }
        });
        /*
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                getdata();
            }
        });*/
        dMinute = dialogBuilder.create();
        dMinute.show();
    }

    public void showUpdateMinuteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_minutes_new, null);
        dialogBuilder.setView(dialogView);
       // dialogBuilder.setTitle("Update Minute");

        toolbar = dialogView.findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Minute");

        accountId = textAccountId.getText().toString();

        min_agenda = dialogView.findViewById(R.id.min_agenda);


        dbhelper = new DBHelper(this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor account = db.query(Database.MINUTE_TABLE_NAME, null,
                " _id = ?", new String[] { accountId }, null, null, null);
        //startManagingCursor(accounts);
        if (account.moveToFirst()) {
            // update view
            min_agenda.setText(account.getString(account
                    .getColumnIndex(Database.MIN_NAME)));




        }
        account.close();
        db.close();
        dbhelper.close();



        float_addminute = dialogView.findViewById(R.id.float_addminute);
        float_addminute.setVisibility(View.GONE);


        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                deleteMinute();

            }
        });
        dialogBuilder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                updateMinute();




            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void updateMinute() {
        try {
            dbhelper = new DBHelper(this);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            // execute insert command

            ContentValues values = new ContentValues();
            values.put( Database.MIN_NAME, min_agenda.getText().toString());



            long rows = db.update(Database.MINUTE_TABLE_NAME, values,
                    "_id = ?", new String[] { accountId });

            db.close();
            if (rows > 0){
                Toast.makeText(this, "Updated Minute Successfully!",
                        Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Sorry! Could not update Minute!",
                        Toast.LENGTH_LONG).show();}
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteMinute() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this Minute?")
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
            int rows = db.delete(Database.MINUTE_TABLE_NAME, "_id=?", new String[]{ accountId});
            dbhelper.close();
            if ( rows == 1) {
                Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_LONG).show();

                //this.finish();

            }
            else{
                Toast.makeText(this, "Could not delete Minute!", Toast.LENGTH_LONG).show();}
            //}

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onStart() {
        super.onStart();

    }




}
