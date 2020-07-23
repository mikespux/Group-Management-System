package com.gms.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gms.R;
import com.gms.cloud.RestApiRequest;
import com.gms.database.DBHelper;
import com.gms.database.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    String accountId;
    EditText et_fullname,et_membercode,et_nationalid,et_email,et_mobileno,et_password,et_cpassword,et_opassword;
    CheckBox checkVisiblePass;
    FloatingActionButton btn_updatepass;
    String s_fullname,s_membercode,s_nationalid,s_email,s_mobileno,s_password,s_cpassword;
    String errors,error,done,fullname,national_id,email,phone,membership_code,updated_at, created_at,id;
    String restApiResponse;
    int response;
    SQLiteDatabase db;
    SharedPreferences prefs ;
    Intent mIntent;
    AlertDialog dMember;
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
       prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        dbhelper = new DBHelper(getApplicationContext());
        lvMembers = this.findViewById(R.id.lvMembers);
        lvMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View selectedView, int arg2, long arg3) {
                textAccountId = selectedView.findViewById(R.id.txtAccountId);
                accountId=textAccountId.getText().toString();
                Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());
             showMemberInfoDialog();

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


    public void showMemberInfoDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_memberinfo, null);
        dialogBuilder.setView(dialogView);
        // dialogBuilder.setTitle("New Event");

        toolbar = dialogView.findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Member Details");

        et_fullname=dialogView.findViewById(R.id.et_fullname);
        et_membercode=dialogView.findViewById(R.id.et_membercode);
        et_nationalid=dialogView.findViewById(R.id.et_nationalid);
        et_email=dialogView.findViewById(R.id.et_email);
        et_mobileno=dialogView.findViewById(R.id.et_mobileno);

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor account = db.query(Database.USERS_TABLE_NAME, null,
                " _id = ?", new String[] { accountId }, null, null, null);
        //startManagingCursor(accounts);
        if (account.moveToFirst()) {
            // update view

            et_fullname.setText(account.getString(account
                    .getColumnIndex(Database.FULLNAME)));

            et_membercode.setText(account.getString(account
                    .getColumnIndex(Database.MEMBERCODE)));
            et_nationalid.setText(account.getString(account
                    .getColumnIndex(Database.NATIONALID)));
            et_email.setText(account.getString(account
                    .getColumnIndex(Database.EMAIL)));
            et_mobileno.setText(account.getString(account
                    .getColumnIndex(Database.MOBILENO)));



        }
        account.close();


        dialogBuilder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                getdata();

            }
        });

        dialogBuilder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                UpdateDetails();
                getdata();
            }
        });
        dMember = dialogBuilder.create();
        dMember.show();
    }

    public void UpdateDetails(){
        try {
            s_fullname = et_fullname.getText().toString();
            s_membercode = et_membercode.getText().toString();
            s_nationalid = et_nationalid.getText().toString();
            s_email = et_email.getText().toString();
            s_mobileno = et_mobileno.getText().toString();



            new Update().execute();

        } catch (Exception e) {

            Toast.makeText(MembersActivity.this, "Update  Failed", Toast.LENGTH_LONG).show();


        }
    }
    private class Update extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MembersActivity.this,
                    "Signing UP",
                    "Wait.. ");
        }


        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()




            try {
                id=prefs.getString("id", "");
                restApiResponse = new RestApiRequest(getApplicationContext()).UpdateUser(id,s_fullname,s_nationalid,s_email,s_mobileno,prefs.getString("pass", ""));


                JSONObject jsonObject = new JSONObject(restApiResponse);
                fullname = jsonObject.getString("fullname");
                membership_code = jsonObject.getString("membership_code");
                national_id = jsonObject.getString("national_id");
                email = jsonObject.getString("email");
                phone = jsonObject.getString("phone");
                created_at = jsonObject.getString("created_at");
                updated_at = jsonObject.getString("updated_at");
                id = jsonObject.getString("id");
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("id", id);
                edit.commit();
                Log.i("INFO", "ID: "+ id+" Title"+ fullname+" Message"+ membership_code);
                try {     progressDialog.dismiss();
                    if (Integer.valueOf(id).intValue() > 0) {


                    }
                    if (Integer.valueOf(id).intValue()<0) {

                        return null;
                    }
                    //System.out.println(value);}
                } catch (NumberFormatException e) {
                    //value = 0; // your default value
                    return null;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            response=prefs.getInt("response",0);
            if(response==200){

                progressDialog.dismiss();


                dbhelper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                // execute insert command

                ContentValues values = new ContentValues();
                values.put( Database.FULLNAME, s_fullname);
                values.put( Database.MEMBERCODE, s_membercode);
                values.put( Database.NATIONALID, s_nationalid);
                values.put( Database.EMAIL,s_email);
                values.put( Database.MOBILENO,s_mobileno);



                long rows = db.update(Database.USERS_TABLE_NAME, values,
                        "_id = ?", new String[] { accountId });

                db.close();
                if (rows > 0){
                    Toast.makeText(getApplicationContext(), "Updated  Successfully!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry! Could not update!",
                            Toast.LENGTH_LONG).show();}

                Log.i("INFO", "ID: " + id + " Title" + fullname + " Message" + membership_code);

                Cursor d = dbhelper.getGivenName(s_mobileno);
                String full_name = d.getString(1);
                Context context = getApplicationContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastroot = inflater.inflate(R.layout.accent_toast, null);
                TextView text = customToastroot.findViewById(R.id.toast);
                text.setText("Successfully Updated " + full_name);
                Toast customtoast = new Toast(context);
                customtoast.setView(customToastroot);
                customtoast.setGravity(Gravity.BOTTOM | Gravity.BOTTOM, 0, 0);
                customtoast.setDuration(Toast.LENGTH_LONG);
                customtoast.show();



                return;
            }

            //finalResult.setText(result);
        }



        @Override
        protected void onProgressUpdate(String... text) {


        }
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
