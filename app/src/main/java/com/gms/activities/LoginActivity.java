package com.gms.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gms.R;
import com.gms.database.DBHelper;
import com.gms.utils.Tools;
import com.google.android.material.snackbar.Snackbar;


public class LoginActivity extends AppCompatActivity {

    private View parent_view;
    Button btnLogin;
    Intent mIntent;
    EditText et_mobileno, et_password;
    String userName,userPassword;
    DBHelper dbhelper;
    SQLiteDatabase db;
    SharedPreferences prefs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_card_light);

        parent_view = findViewById(android.R.id.content);

        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent= new Intent(getApplicationContext(), SigupActivity.class);
                startActivity(mIntent);
                Snackbar.make(parent_view, "Sign Up", Snackbar.LENGTH_SHORT).show();
            }
        });

        Tools.setSystemBarColor(this);
        initializer();
    }

    public void initializer(){
        dbhelper = new DBHelper(getApplicationContext());
        db= dbhelper.getReadableDatabase();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        et_mobileno = findViewById(R.id.et_mobileno);
        et_password = findViewById(R.id.et_password);


        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = et_mobileno.getText().toString().trim();
                userPassword = et_password.getText().toString().trim();
                if (!android.util.Patterns.PHONE.matcher(userName).matches()) {

                    et_mobileno.setError("Mobile No is not valid");
                    Toast.makeText(getApplicationContext(), "Mobile No is not valid!",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userName.length() <7 ||userName.length() >14) {
                    et_mobileno.setError("Enter a valid MobileNo");
                } else if (userPassword.length() < 3) {
                    et_password.setError("Invalid Password");
                } else {


                    if (dbhelper.UserLogin(userName, userPassword)) {
                        // save user data
                        SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(LoginActivity.this);

                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("user", userName);
                        edit.commit();

                        edit.putString("pass", userPassword);
                        edit.commit();

                        Cursor d = dbhelper.getGivenName(userName);
                        String full_name = d.getString(1);
                        Context context = getApplicationContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastroot = inflater.inflate(R.layout.accent_toast, null);
                        TextView text = customToastroot.findViewById(R.id.toast);
                        text.setText("Successfully Logged In " + full_name);
                        Toast customtoast = new Toast(context);
                        customtoast.setView(customToastroot);
                        customtoast.setGravity(Gravity.BOTTOM | Gravity.BOTTOM, 0, 0);
                        customtoast.setDuration(Toast.LENGTH_LONG);
                        customtoast.show();


                        finish();
                        Intent login = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(login);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();

                    }
                    dbhelper.close();
                }
            }
        });
    }

    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String uname = prefs.getString("user", "");
        String pass = prefs.getString("pass", "");

        String userName = uname;
        String userPassword = pass;
        et_mobileno.setText(uname);

        try{
            if(userName.length() > 0 && userPassword.length() >0)
            {
                DBHelper dbUser = new DBHelper(LoginActivity.this);


                if(dbUser.UserLogin(userName, userPassword))
                {



                    finish();
                    Intent login =new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(login);
                }else{

                }
                dbUser.close();
            }

        }catch(Exception e)
        {
            Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}

