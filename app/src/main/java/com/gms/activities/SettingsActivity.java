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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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


public class SettingsActivity extends AppCompatActivity {
    Boolean success = true;
    Intent mIntent;
    public Toolbar toolbar;
    FloatingActionButton btn_update;
    Button btn_changepass;

    String accountId;
    EditText et_fullname,et_membercode,et_nationalid,et_email,et_mobileno,et_password,et_cpassword,et_opassword;
    CheckBox checkVisiblePass;
    FloatingActionButton btn_updatepass;
    String s_fullname,s_membercode,s_nationalid,s_email,s_mobileno,s_password,s_cpassword;
    String errors,error,done,fullname,national_id,email,phone,membership_code,updated_at, created_at,id;
    DBHelper dbhelper;
    SQLiteDatabase db;
    SharedPreferences prefs ;
    String restApiResponse;
    int response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_userinfo);
        setupToolbar();
        initializer();
    }

    public void setupToolbar() {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.menu_settings);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void onBackPressed() {
        //Display alert message when back button has been pressed
        finish();
        mIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mIntent);
        return;
    }
    public void initializer(){
        dbhelper = new DBHelper(getApplicationContext());
        db= dbhelper.getReadableDatabase();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Cursor d = dbhelper.getGivenName(prefs.getString("user", ""));
        accountId = d.getString(0);
        btn_update=findViewById(R.id.btn_update);
        btn_changepass=findViewById(R.id.btn_changepass);



        et_fullname=findViewById(R.id.et_fullname);
        et_membercode=findViewById(R.id.et_membercode);
        et_nationalid=findViewById(R.id.et_nationalid);
        et_email=findViewById(R.id.et_email);
        et_mobileno=findViewById(R.id.et_mobileno);






        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_fullname = et_fullname.getText().toString();
                s_membercode = et_membercode.getText().toString();
                if (s_fullname.equals("")||s_fullname.length()<=0) {
                    et_fullname.setError("Please Enter FullName!");
                    Toast.makeText(getApplicationContext(), "Please Enter FullName!", Toast.LENGTH_LONG).show();
                    return;
                }

                s_nationalid = et_nationalid.getText().toString();
                s_email = et_email.getText().toString();
                if (s_nationalid.equals("")||s_nationalid.length()<=0) {
                    et_nationalid.setError("Please Enter NationalID!");
                    Toast.makeText(getApplicationContext(), "Please Enter NationalID!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (s_nationalid.length() <7 ||s_nationalid.length() >8 ) {

                    et_nationalid.setError("Enter a valid IDNo!");
                    Toast.makeText(getApplicationContext(), "Enter a valid IDNo!",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (s_email.equals("")||s_email.length()<=0) {
                    et_email.setError("Please Enter Email!");
                    Toast.makeText(getApplicationContext(), "Please Enter Email!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s_email).matches()) {
                    et_email.setError("Invalid Email address");
                    Toast.makeText(getApplicationContext(), "Invalid Email address!",Toast.LENGTH_SHORT).show();

                    return;
                }
              UpdateDetails();


            }
        });

        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

    }
    public void changePassword(){


        AlertDialog.Builder dialogFarmerSearch = new AlertDialog.Builder(this);
        LayoutInflater inflater1 = this.getLayoutInflater();
        final View dialogView = inflater1.inflate(R.layout.dialog_change_pass, null);
        dialogFarmerSearch.setView(dialogView);
        dialogFarmerSearch.setCancelable(true);
    //    dialogFarmerSearch.setTitle("Change Password");
        toolbar = dialogView.findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");


        et_opassword=dialogView.findViewById(R.id.et_opassword);
        et_password=dialogView.findViewById(R.id.et_password);
        et_cpassword=dialogView.findViewById(R.id.et_cpassword);


        checkVisiblePass= dialogView.findViewById(R.id.checkVisiblePass);
        checkVisiblePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //do stuff
                if(checkVisiblePass.isChecked()){
                    et_opassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{

                    et_opassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_updatepass= dialogView.findViewById(R.id.btn_updatepass);
        btn_updatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor d = dbhelper.getPassword(prefs.getString("user", ""));
                String userpass = d.getString(0);
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                //Toast.makeText(v.getContext(),"UserName: "+username+" Pass: "+userpass,Toast.LENGTH_SHORT).show();
                Cursor a = dbhelper.getGivenName(prefs.getString("user", ""));
                accountId = a.getString(0);
                String opassword = et_opassword.getText().toString();
                String password = et_password.getText().toString();
                String cpassword = et_cpassword.getText().toString();
                if (opassword.length() <4) {
                    et_opassword.setError("Invalid Password Length");
                    return;
                }
                if (password.length() <4) {
                    et_password.setError("Invalid Password Length");
                    return;
                }
                if (cpassword.length() <4) {
                    et_cpassword.setError("Invalid Password Length");
                    return;
                }

                if(!opassword.equals(userpass))
                {
                    Toast.makeText(getApplicationContext(), "Invalid Old Password", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!cpassword.equals(password))
                {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put( Database.PASSWORD, password);
                long rows = db.update(Database.USERS_TABLE_NAME, values,
                        "_id = ?", new String[] { accountId });

                db.close();
                if (rows > 0){
                    Toast.makeText(getApplicationContext(), "Updated Password Successfully!",Toast.LENGTH_LONG).show();
                    et_opassword.setText("");
                    et_password.setText("");
                    et_cpassword.setText("");
                    new LogOut().execute();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry! Could not update Password!",
                            Toast.LENGTH_LONG).show();}

            }
        });

        dialogFarmerSearch.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int whichButton) {




            }
        });


           /* dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                    getdata();
                }
            });*/
        AlertDialog changepass = dialogFarmerSearch.create();
        changepass.show();
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

                Toast.makeText(SettingsActivity.this, "Update  Failed", Toast.LENGTH_LONG).show();


        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onStart() {
        super.onStart();
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
       // db.close();
       // dbhelper.close();

    }
    private class Update extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SettingsActivity.this,
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
    private class LogOut extends AsyncTask<Void, Void, String>
    {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            dialog = ProgressDialog.show( SettingsActivity.this,
                    getString(R.string.please_wait),
                    getString(R.string.logging_out),
                    true);
        }

        @Override
        protected String doInBackground(Void... params)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
            SharedPreferences.Editor edit = prefs.edit();
            edit.remove("pass");
            edit.commit();



            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {

            dialog.dismiss();
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addCategory(android.content.Intent.CATEGORY_HOME);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


}
