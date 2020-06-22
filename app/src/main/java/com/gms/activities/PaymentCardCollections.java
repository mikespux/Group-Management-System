package com.gms.activities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gms.R;
import com.gms.utils.Tools;


public class PaymentCardCollections extends AppCompatActivity {

    private static final int MAX_STEP = 3;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    private String postfix_array[] = {
            "**** **** **** ****",
            "**** **** **** 6223",
            "**** **** **** 1027"
    };
    private String expire_array[] = {
            "-----",
            "08/20",
            "11/23"
    };
    private String cvv_array[] = {
            "---",
            "771",
            "098"
    };
    private int logo_array[] = {
            R.drawable.ic_mpesa,
            R.drawable.ic_visa_new,
            R.drawable.ic_mastercard_new
    };

    private int color_array[] = {
            R.color.green_500,
            R.color.deep_orange_800,
            R.color.blue_500
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_card_collections);
        initToolbar();

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin_overlap_payment));
        viewPager.setOffscreenPageLimit(MAX_STEP);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.changeNavigateionIconColor(toolbar, getResources().getColor(R.color.colorPrimary));
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.light_green_600), PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_card_payment, container, false);
            ((TextView) view.findViewById(R.id.card_number)).setText(postfix_array[position]);
            ((TextView) view.findViewById(R.id.expire)).setText(expire_array[position]);
            ((TextView) view.findViewById(R.id.cvv)).setText(cvv_array[position]);
            ((ImageView) view.findViewById(R.id.card_logo)).setImageResource(logo_array[position]);
            ((CardView) view.findViewById(R.id.card)).setCardBackgroundColor(getResources().getColor(color_array[position]));

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return MAX_STEP;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
