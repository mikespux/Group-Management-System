package com.gms.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gms.R;
import com.gms.database.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SigupActivity extends AppCompatActivity {
    Boolean success = true;
    Intent mIntent;
    public Toolbar toolbar;
    LinearLayout LtPage1,LtPage2,LtPage3,LtPage4;
    FloatingActionButton btn_pg1,btn_pg2,btn_pg3;
    Button btn_pg4;

    EditText et_fullname,et_membercode,et_nationalid,et_email,et_mobileno,et_password,et_cpassword,et_vcode;
    String s_fullname,s_membercode,s_nationalid,s_email,s_mobileno,s_password,s_cpassword,s_vcode;
    DBHelper dbhelper;
    SQLiteDatabase db;
    SharedPreferences prefs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupToolbar();
        initializer();
    }

    public void setupToolbar() {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.signup);

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
        db= dbhelper.getReadableDatabase();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        LtPage1=findViewById(R.id.LtPage1);
        LtPage2=findViewById(R.id.LtPage2);
        LtPage3=findViewById(R.id.LtPage3);
        LtPage4=findViewById(R.id.LtPage4);

        btn_pg1=findViewById(R.id.btn_pg1);
        btn_pg2=findViewById(R.id.btn_pg2);
        btn_pg3=findViewById(R.id.btn_pg3);
        btn_pg4=findViewById(R.id.btn_pg4);


        et_fullname=findViewById(R.id.et_fullname);
        et_membercode=findViewById(R.id.et_membercode);
        et_nationalid=findViewById(R.id.et_nationalid);
        et_email=findViewById(R.id.et_email);
        et_mobileno=findViewById(R.id.et_mobileno);
        et_password=findViewById(R.id.et_password);
        et_cpassword=findViewById(R.id.et_cpassword);
        et_vcode=findViewById(R.id.et_vcode);






        btn_pg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_fullname = et_fullname.getText().toString();
                s_membercode = et_membercode.getText().toString();
                if (s_fullname.equals("")||s_fullname.length()<=0) {
                    et_fullname.setError("Please Enter FullName!");
                    Toast.makeText(getApplicationContext(), "Please Enter FullName!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (s_membercode.equals("")||s_membercode.length()<=0) {
                    et_membercode.setError("Please Enter Membership Code!");
                    Toast.makeText(getApplicationContext(), "Please Enter Membership Code!", Toast.LENGTH_LONG).show();
                    return;
                }

                Cursor membercode =dbhelper.fetchMemberCode(s_membercode);
                //Check for duplicate Membership Code
                if (membercode.getCount() > 0) {
                    Toast.makeText(getApplicationContext(), "Membership Code already exists",Toast.LENGTH_SHORT).show();
                    return;
                }

                LtPage1.setVisibility(View.GONE);
                LtPage2.setVisibility(View.VISIBLE);
                LtPage3.setVisibility(View.GONE);
                LtPage4.setVisibility(View.GONE);
            }
        });
        btn_pg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                Cursor nationalid =dbhelper.fetchNationalID(s_nationalid);
                //Check for duplicate NationalID
                if (nationalid.getCount() > 0) {
                    Toast.makeText(getApplicationContext(), "National ID already exists",Toast.LENGTH_SHORT).show();
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

                LtPage1.setVisibility(View.GONE);
                LtPage2.setVisibility(View.GONE);
                LtPage3.setVisibility(View.VISIBLE);
                LtPage4.setVisibility(View.GONE);
            }
        });
        btn_pg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_mobileno = et_mobileno.getText().toString();
                s_password = et_password.getText().toString();
                s_cpassword = et_cpassword.getText().toString();

                if (s_mobileno.equals("")||s_mobileno.length()<=0) {
                    et_mobileno.setError("Please Enter Mobile No!");
                    Toast.makeText(getApplicationContext(), "Please Enter Mobile No!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (s_mobileno.length() <7 ||s_mobileno.length() >14) {
                    et_mobileno.setError("Enter a valid MobileNo");
                    Toast.makeText(getApplicationContext(), "Mobile No is not valid!",Toast.LENGTH_SHORT).show();
                }
                if (!android.util.Patterns.PHONE.matcher(s_mobileno).matches()) {

                    et_mobileno.setError("Mobile No is not valid");
                    Toast.makeText(getApplicationContext(), "Mobile No is not valid!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor mobileno =dbhelper.fetchMobileNo(s_mobileno);
                //Check for duplicate Mobile No
                if (mobileno.getCount() > 0) {
                    et_mobileno.setError("Mobile No already exists");
                    Toast.makeText(getApplicationContext(), "Mobile No already exists",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s_password.equals("")||s_password.length()<=0) {
                    et_password.setError("Please Enter Password!");
                    Toast.makeText(getApplicationContext(), "Please Enter Password!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (s_cpassword.equals("")||s_cpassword.length()<=0) {
                    et_cpassword.setError("Please Confirm Password!");
                    Toast.makeText(getApplicationContext(), "Please Confirm Password!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!s_password.equals(s_cpassword)){
                    Toast.makeText(getApplicationContext(), "Password Does Not Match!!", Toast.LENGTH_LONG).show();
                    return;
                }
                LtPage1.setVisibility(View.GONE);
                LtPage2.setVisibility(View.GONE);
                LtPage3.setVisibility(View.GONE);
                LtPage4.setVisibility(View.VISIBLE);
            }
        });
        btn_pg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_vcode = et_vcode.getText().toString();
                if(s_vcode.equals("0000")){

                    SaveDetails();
                    finish();
                    mIntent= new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mIntent);
                }else{
                    et_vcode.setError("Invalid Verification Code!");
                    Toast.makeText(getApplicationContext(), "Invalid Verification Code!", Toast.LENGTH_LONG).show();

                }



            }
        });
    }

    public void SaveDetails(){
        try {
            s_fullname = et_fullname.getText().toString();
            s_membercode = et_membercode.getText().toString();
            s_nationalid = et_nationalid.getText().toString();
            s_email = et_email.getText().toString();
            s_mobileno = et_mobileno.getText().toString();
            s_password = et_password.getText().toString();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SigupActivity.this);

            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("user", s_mobileno);
            edit.commit();

            edit.putString("pass", s_password);
            edit.commit();


            dbhelper.AddUsers(s_fullname,s_membercode,s_nationalid,s_email,s_mobileno,s_password);
            if (success) {
                Cursor d = dbhelper.getGivenName(s_mobileno);
                String full_name = d.getString(1);
                Context context = getApplicationContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastroot = inflater.inflate(R.layout.accent_toast, null);
                TextView text = customToastroot.findViewById(R.id.toast);
                text.setText("Successfully Registered " + full_name);
                Toast customtoast = new Toast(context);
                customtoast.setView(customToastroot);
                customtoast.setGravity(Gravity.BOTTOM | Gravity.BOTTOM, 0, 0);
                customtoast.setDuration(Toast.LENGTH_LONG);
                customtoast.show();

                //Toast.makeText(SigupActivity.this, "User Registered successfully!!", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            success = false;

            if (success) {
                Toast.makeText(SigupActivity.this, "Saving  Failed", Toast.LENGTH_LONG).show();
            }

        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onStart() {
        super.onStart();

    }




}
