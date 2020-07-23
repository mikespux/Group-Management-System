package com.gms.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gms.R;
import com.gms.database.DBHelper;
import com.gms.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    FrameLayout container;
    NavigationView navigationView;
    DrawerLayout mDrawerLayout;
    Fragment frag = null;
    LinearLayout ltLogout;
    DBHelper dbhelper;
    SQLiteDatabase db;
    SharedPreferences prefs;
    TextView userid;
    Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        dbhelper = new DBHelper(getApplicationContext());
        db= dbhelper.getReadableDatabase();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
      navigationView = findViewById(R.id.nav_view);
       container= findViewById(R.id.container);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        View header=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                displayView(menuItem.getItemId());
              //  Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();

                return true;
            }
        });
        frag = new HomeFragment();
        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.add(R.id.container, frag, frag.getTag());
            ft.replace(R.id.container,  frag, frag.getTag());
            ft.commit();
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        userid= header.findViewById(R.id.userid);
        Cursor d = dbhelper.getGivenName(prefs.getString("user", ""));
        String user_fullname = d.getString(1);
        SharedPreferences.Editor edit1 = prefs.edit();
        edit1.putString("fullname", user_fullname);
        edit1.commit();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            // Toast.makeText(getActivity(), "Good Morning", Toast.LENGTH_SHORT).show();
            userid.setText("Good Morning!\n" + user_fullname);
        }else if(timeOfDay >= 12 && timeOfDay < 16){

            userid.setText("Good Afternoon!\n" + user_fullname);
    }
        else if(timeOfDay >= 16 && timeOfDay < 21){

            userid.setText("Good Evening!\n" + user_fullname);
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            userid.setText("Good Night!\n" + user_fullname);
    }
   ltLogout=findViewById(R.id.ltLogout);
        ltLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButtonHandler();
            }
        });


    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void displayView(int id) {
        switch (id) {
            case R.id.nav_home:
                frag = new HomeFragment();
                if (frag != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    //ft.add(R.id.container, frag, frag.getTag());
                    ft.replace(R.id.container,  frag, frag.getTag());
                    ft.commit();
                }
                break;

            case R.id.nav_chat:
                mIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(mIntent);
                break;
            case R.id.nav_reports:
                mIntent = new Intent(getApplicationContext(), ReportsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.nav_notifications:
                mIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(mIntent);
                break;
            case R.id.nav_settings:
                finish();
                mIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(mIntent);
                break;


        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        backButtonHandler();
    }
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Close GMS?");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to close the application?");

        // Setting Positive "Yes" Button
        alertDialog.setNegativeButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new LogOut().execute();
                        // stopService(new Intent(MainActivity.this, WeighingService.class));


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
    private class LogOut extends AsyncTask<Void, Void, String>
    {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            dialog = ProgressDialog.show( MainActivity.this,
                    getString(R.string.please_wait),
                    getString(R.string.logging_out),
                    true);
        }

        @Override
        protected String doInBackground(Void... params)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
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
