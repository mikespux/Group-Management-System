package com.gms.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.gms.R;
import com.gms.database.DBHelper;
import com.gms.database.Database;


import java.util.ArrayList;

/**
 * Created by Michael on 30/06/2016.
 */
public class MembersActivity extends AppCompatActivity {
    public Toolbar toolbar;
    DBHelper dbhelper;

    ListView lvMembers;

    TextView textAccountId;
    Boolean success = true;
    static SharedPreferences mSharedPrefs;

    SearchView searchView;
    public SimpleCursorAdapter ca;
    Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_listmembers);
        setupToolbar();
        initializer();
    }

    public void setupToolbar() {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.menu_members);

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
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        dbhelper = new DBHelper(getApplicationContext());
        lvMembers = this.findViewById(R.id.lvMembers);
        lvMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View selectedView, int arg2, long arg3) {
                textAccountId = selectedView.findViewById(R.id.txtAccountId);
                Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());


            }
        });

        searchView= findViewById(R.id.searchView);
        searchView.setQueryHint("Search Member ...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                ca.getFilter().filter(query);
                ca.setFilterQueryProvider(new FilterQueryProvider() {

                    @Override
                    public Cursor runQuery(CharSequence constraint) {
                        String member = constraint.toString();

                        return dbhelper.SearchSpecificMember(member);

                    }
                });
                // Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ca.getFilter().filter(newText);
                ca.setFilterQueryProvider(new FilterQueryProvider() {

                    @Override
                    public Cursor runQuery(CharSequence constraint) {
                        String member = constraint.toString();
                        return dbhelper.SearchMember(member);

                    }
                });
                //Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });
       searchView.requestFocus();



    }





    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onStart() {
        super.onStart();
        getdata();
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void getdata(){

        try {

            SQLiteDatabase db= dbhelper.getReadableDatabase();
           Cursor accounts = db.query(true, Database.USERS_TABLE_NAME, null, null, null, null, null, null, null, null);
/*if(accounts.getCount()==0)
    Toast.makeText(this, "no records", Toast.LENGTH_LONG).show();*/
            String from [] = {  Database.ROW_ID,Database.MEMBERCODE , Database.FULLNAME, Database.NATIONALID, Database.MOBILENO};
            int to [] = { R.id.txtAccountId,R.id.tv_membercode,R.id.tv_name,R.id.tv_nationalid,R.id.tv_phone};


            ca  = new SimpleCursorAdapter(this,R.layout.member_item, accounts,from,to);

            ListView lvMembers= this.findViewById( R.id.lvMembers);
            lvMembers.setAdapter(ca);
            lvMembers.setTextFilterEnabled(true);
            dbhelper.close();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed() {
        //Display alert message when back button has been pressed
        finish();

        return;
    }


}
