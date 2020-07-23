package com.gms.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.gms.R;
import com.gms.chat.adapter;
import com.gms.chat.list;
import com.gms.chat.notadapter;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private SwipeMenuListView listView;
    private ArrayList<list> dataArrayList;
    private notadapter listAdapter;
    private list data;
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        setupToolbar();
        listView = (SwipeMenuListView) findViewById(R.id.listview);
        dataArrayList = new ArrayList<>();


        dataArrayList.add(data = new list("New Vote", "Election Ready",R.drawable.no_profile));
        dataArrayList.add(data = new list("PaymentActivity Due", "Finished payments",R.drawable.no_profile));
        dataArrayList.add(data = new list("2019 PL Ready", "Profit and Loss",R.drawable.no_profile));


        listAdapter = new notadapter(this, dataArrayList);
        listView.setAdapter(listAdapter);

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        Toast.makeText(NotificationActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                        dataArrayList.remove(position);
                        listAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        break;
                }
                return false;
            }
        });

    }


    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.parseColor("#F45557")));
            // set item width
            deleteItem.setWidth(150);
            deleteItem.setTitle("x");
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(15);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    public void setupToolbar() {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notifications");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
