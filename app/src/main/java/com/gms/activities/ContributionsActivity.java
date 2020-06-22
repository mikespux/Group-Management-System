package com.gms.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gms.R;


public class ContributionsActivity extends AppCompatActivity {
    public Toolbar toolbar;
    // Array of strings...
    String[] categoryArray = {"1. Developement Harambee","2. Youth Conference","3. Membership Renewal"};
    ListView categoryList;
    Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributions);
        setupToolbar();
        initializer();
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

    public void initializer(){

        ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.contribution_category, categoryArray);

        categoryList = findViewById(R.id.category_list);
        categoryList.setAdapter(adapter);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mIntent= new Intent(getApplicationContext(), ContributeActivity.class);
                startActivity(mIntent);
            }
        });


    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onStart() {
        super.onStart();

    }




}
