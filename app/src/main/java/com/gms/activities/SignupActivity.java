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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity {
    Boolean success = true;
    Intent mIntent;
    public Toolbar toolbar;
    LinearLayout LtPage1,LtPage2,LtPage3,LtPage4;
    FloatingActionButton btn_pg1,btn_pg2,btn_pg3;
    FloatingActionButton btn_bpg2,btn_bpg3,btn_bpg4;
    Button btn_pg4;

    EditText et_fullname,et_membercode,et_nationalid,et_email,et_mobileno,et_password,et_cpassword,et_vcode;
    String s_fullname,s_membercode,s_nationalid,s_email,s_mobileno,s_password,s_cpassword,s_vcode;
    DBHelper dbhelper;
    SQLiteDatabase db;
    SharedPreferences prefs ;
    String restApiResponse;
    CheckBox checkVisiblePass;
    String errors,error,done,fullname,national_id,email,phone,membership_code,updated_at, created_at,id;
    String verify_code;
   int response;
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
        Cursor code = dbhelper.fetchCode();
        if(code.getCount()>0){


            code.moveToFirst();
            Log.i("Code Count",String.valueOf(code.getCount()));
            Log.i("Verification Code",code.getString(code.getColumnIndex(Database.VERYCODE)));
            if(Integer.parseInt(code.getString(code.getColumnIndex(Database.VERYCODE)))==0){

                LtPage1.setVisibility(View.GONE);
                LtPage2.setVisibility(View.GONE);
                LtPage3.setVisibility(View.GONE);
                LtPage4.setVisibility(View.VISIBLE);

            }
        }
        btn_pg1=findViewById(R.id.btn_pg1);
        btn_pg2=findViewById(R.id.btn_pg2);
        btn_pg3=findViewById(R.id.btn_pg3);
        btn_pg4=findViewById(R.id.btn_pg4);

        btn_bpg2=findViewById(R.id.btn_bpg2);
        btn_bpg3=findViewById(R.id.btn_bpg3);
        btn_bpg4=findViewById(R.id.btn_bpg4);

        btn_bpg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LtPage1.setVisibility(View.VISIBLE);
                LtPage2.setVisibility(View.GONE);
                LtPage3.setVisibility(View.GONE);
                LtPage4.setVisibility(View.GONE);
            }
        });
        btn_bpg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LtPage1.setVisibility(View.GONE);
                LtPage2.setVisibility(View.VISIBLE);
                LtPage3.setVisibility(View.GONE);
                LtPage4.setVisibility(View.GONE);
            }
        });
        btn_bpg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LtPage1.setVisibility(View.GONE);
                LtPage2.setVisibility(View.GONE);
                LtPage3.setVisibility(View.VISIBLE);
                LtPage4.setVisibility(View.GONE);
            }
        });




        et_fullname=findViewById(R.id.et_fullname);
        et_membercode=findViewById(R.id.et_membercode);
        et_nationalid=findViewById(R.id.et_nationalid);
        et_email=findViewById(R.id.et_email);
        et_mobileno=findViewById(R.id.et_mobileno);
        et_password=findViewById(R.id.et_password);
        et_cpassword=findViewById(R.id.et_cpassword);
        et_vcode=findViewById(R.id.et_vcode);

        checkVisiblePass= findViewById(R.id.checkVisiblePass);
        checkVisiblePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //do stuff
                if(checkVisiblePass.isChecked()){
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{

                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });




        btn_pg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                s_fullname = et_fullname.getText().toString();
                s_nationalid = et_nationalid.getText().toString();
                s_membercode = et_membercode.getText().toString();
                if (s_fullname.equals("")||s_fullname.length()<=0) {
                    et_fullname.setError("Please Enter FullName!");
                    Toast.makeText(getApplicationContext(), "Please Enter FullName!", Toast.LENGTH_LONG).show();
                    return;
                }

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
                    //Toast.makeText(getApplicationContext(), "National ID already exists",Toast.LENGTH_SHORT).show();
                    //return;
                }



                if (s_membercode.equals("")||s_membercode.length()<=0) {
                    //et_membercode.setError("Please Enter Membership Code!");
                  //  Toast.makeText(getApplicationContext(), "Please Enter Membership Code!", Toast.LENGTH_LONG).show();
                  //  return;
                }

                Cursor membercode =dbhelper.fetchMemberCode(s_membercode);
                //Check for duplicate Membership Code
                if (membercode.getCount() > 0) {
                   // Toast.makeText(getApplicationContext(), "Membership Code already exists",Toast.LENGTH_SHORT).show();
                   // return;
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
                s_mobileno = et_mobileno.getText().toString();
                s_email = et_email.getText().toString();
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
                  //  et_mobileno.setError("Mobile No already exists");
                    //Toast.makeText(getApplicationContext(), "Mobile No already exists",Toast.LENGTH_SHORT).show();
                    //  return;
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

                s_password = et_password.getText().toString();
                s_cpassword = et_cpassword.getText().toString();


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
                List<String> errorList=isValid(s_password);
                if (!errorList.isEmpty()) {
                    System.out.println("The password entered here  is invalid");
                    et_password.setError(errorList.toString().replaceAll("[,]",""));
                    Toast.makeText(getApplicationContext(), errorList.toString().replaceAll("[,]",""), Toast.LENGTH_LONG).show();
                   return;
                }


                SaveDetails();

            }
        });
        btn_pg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               new Verify().execute();



            }
        });
    }
    public static List<String> isValid(String passwordhere) {

        List<String> errorList = new ArrayList<String>();

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        if (passwordhere.length() <= 8) {
            errorList.add("Password length must have at least 8 character !!\n");
        }
        if (!specailCharPatten.matcher(passwordhere).find()) {
            errorList.add("Password must have at least one special character !!\n");
        }
        if (!UpperCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have at least one uppercase character !!\n");
        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have at least one lowercase character !!\n");
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have at least one digit character !!\n");
        }

        return errorList;

    }
    public void SaveDetails(){
        try {
            s_fullname = et_fullname.getText().toString();
            s_membercode = et_membercode.getText().toString();
            s_nationalid = et_nationalid.getText().toString();
            s_email = et_email.getText().toString();
            s_mobileno = et_mobileno.getText().toString();
            s_password = et_password.getText().toString();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);

            SharedPreferences.Editor edit = prefs.edit();
        //    edit.putString("user", s_mobileno);
            edit.commit();

         //   edit.putString("pass", s_password);
            edit.commit();

         new Register().execute();


        } catch (Exception e) {


        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onStart() {
        super.onStart();

    }
    private class Verify extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SignupActivity.this,
                    "Verifying",
                    "Wait.. ");
        }

        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()


            try {
                id=prefs.getString("id","");
                verify_code=et_vcode.getText().toString();
                restApiResponse = new RestApiRequest(getApplicationContext()).verify(id,verify_code);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation


            response=prefs.getInt("response",0);
            if(response==200){
                ContentValues values = new ContentValues();
                //values.put(Database.BatCloudID, serverBatchNo);
                values.put(Database.VERYCODE,verify_code);
                long rows = db.update(Database.USERS_TABLE_NAME, values,
                        Database.VERYCODE + " = ?", new String[]{"0"});

                if (rows > 0) {
                    finish();
                    mIntent= new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mIntent);
                    Toast.makeText(getApplicationContext(), restApiResponse, Toast.LENGTH_LONG).show();
                }

            }



        }




        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
    private class Register extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SignupActivity.this,
                    "Signing UP",
                    "Wait.. ");
        }


        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()




            try {
                restApiResponse = new RestApiRequest(getApplicationContext()).Register(s_fullname,s_nationalid,s_email,s_mobileno,s_password);


                JSONObject jsonObject = new JSONObject(restApiResponse);
                errors="";
                if(!jsonObject.isNull("errors")){

                    errors = jsonObject.getString("errors");
                    JSONObject jsonObj = new JSONObject(errors);
                        error="";
                        if(!jsonObj.isNull("national_id")){
                        error=(jsonObj.getString("national_id"));
                        }
                        if(!jsonObj.isNull("email")){
                            error=error+"\n"+(jsonObj.getString("email"));
                        }
                        if(!jsonObj.isNull("phone")){
                            error=error+"\n"+(jsonObj.getString("phone"));
                        }

                        if(!jsonObj.isNull("password")){
                            error=error+"\n"+(jsonObj.getString("password"));
                        }





                    return null;
                }


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
                        dbhelper.AddUsers(s_fullname,membership_code,s_nationalid,s_email,s_mobileno,s_password);

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
            if(!errors.equals("")){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }else {
                if (Integer.valueOf(id).intValue() > 0) {

                    progressDialog.dismiss();


                    Log.i("INFO", "ID: " + id + " Title" + fullname + " Message" + membership_code);

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

                        //Toast.makeText(SignupActivity.this, "User Registered successfully!!", Toast.LENGTH_LONG).show();
                        //new Restart().execute();
                        LtPage1.setVisibility(View.GONE);
                        LtPage2.setVisibility(View.GONE);
                        LtPage3.setVisibility(View.GONE);
                        LtPage4.setVisibility(View.VISIBLE);


                    return;
                }
            }
            //finalResult.setText(result);
        }



        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SignupActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Go Back?");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want cancel signup?");

        // Setting Positive "Yes" Button
        alertDialog.setNegativeButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       finish();


                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setPositiveButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }
}
