package com.gms.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gms.R;
import com.gms.fragments.DialogPaymentSuccessFragment;
import com.gms.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;


public class PaymentActivity extends AppCompatActivity {
    public Toolbar toolbar;
    FloatingActionButton floating_action_button;
    Intent mIntent;
    RadioButton rad_mpesa,rad_card;
    LinearLayout LtMpesa,LtCard;
    SharedPreferences prefs;
    EditText et_amount;
    ProgressBar progress_bar,progress_bar1;
    Button btn_pay,btn_pay1;
    private TextView card_number;
    private TextView card_expire;
    private TextView card_cvv;
    private TextView card_name;

    private TextInputEditText et_card_number;
    private TextInputEditText et_expire;
    private TextInputEditText et_cvv;
    private TextInputEditText et_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setupToolbar();
        initializer();
    }

    public void setupToolbar() {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PAYMENT");

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
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        et_amount=findViewById(R.id.et_amount);
        et_amount.setText(prefs.getString("et_amount","0"));
        progress_bar=findViewById(R.id.progress_bar);
        progress_bar1=findViewById(R.id.progress_bar1);
        btn_pay=findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit1 = prefs.edit();
                edit1.putString("type", "1");
                edit1.commit();
                submitAction();
            }
        });
        btn_pay1=findViewById(R.id.btn_pay1);
        btn_pay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit1 = prefs.edit();
                edit1.putString("type", "2");
                edit1.commit();
                submitAction();
            }
        });
  LtCard=findViewById(R.id.LtCard);
  LtMpesa=findViewById(R.id.LtMpesa);


  rad_card=findViewById(R.id.rad_card);
  rad_mpesa=findViewById(R.id.rad_mpesa);
        rad_mpesa.setChecked(true);
        if(rad_mpesa.isChecked()){
            rad_card.setChecked(false);
            LtMpesa.setVisibility(View.VISIBLE);
            LtCard.setVisibility(View.GONE);
        }
        rad_mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rad_mpesa.isChecked()){
                    rad_card.setChecked(false);
                }
                LtMpesa.setVisibility(View.VISIBLE);
                LtCard.setVisibility(View.GONE);
            }
        });
        if(rad_card.isChecked()){
            rad_mpesa.setChecked(false);
            LtMpesa.setVisibility(View.GONE);
            LtCard.setVisibility(View.VISIBLE);
        }
        rad_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rad_card.isChecked()){
                    rad_mpesa.setChecked(false);
                }
                LtMpesa.setVisibility(View.GONE);
                LtCard.setVisibility(View.VISIBLE);
            }
        });
        card_number = (TextView) findViewById(R.id.card_number);
        card_expire = (TextView) findViewById(R.id.card_expire);
        card_cvv = (TextView) findViewById(R.id.card_cvv);
        card_name = (TextView) findViewById(R.id.card_name);

        et_card_number = (TextInputEditText) findViewById(R.id.et_card_number);
        et_expire = (TextInputEditText) findViewById(R.id.et_expire);
        et_cvv = (TextInputEditText) findViewById(R.id.et_cvv);
        et_name = (TextInputEditText) findViewById(R.id.et_name);

        et_card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (charSequence.toString().trim().length() == 0) {
                    card_number.setText("**** **** **** ****");
                } else {
                    String number = Tools.insertPeriodically(charSequence.toString().trim(), " ", 4);
                    card_number.setText(number);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_expire.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (charSequence.toString().trim().length() == 0) {
                    card_expire.setText("MM/YY");
                } else {
                    String exp = Tools.insertPeriodically(charSequence.toString().trim(), "/", 2);
                    card_expire.setText(exp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (charSequence.toString().trim().length() == 0) {
                    card_cvv.setText("***");
                } else {
                    card_cvv.setText(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (charSequence.toString().trim().length() == 0) {
                    card_name.setText("Your Name");
                } else {
                    card_name.setText(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void submitAction() {
        progress_bar.setVisibility(View.VISIBLE);
        progress_bar1.setVisibility(View.VISIBLE);
        btn_pay.setAlpha(0f);
        btn_pay1.setAlpha(0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialogPaymentSuccess();
                progress_bar.setVisibility(View.GONE);
                progress_bar1.setVisibility(View.GONE);
                btn_pay.setAlpha(1f);
                btn_pay1.setAlpha(1f);
            }
        }, 1000);
    }

    private void showDialogPaymentSuccess() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogPaymentSuccessFragment newFragment = new DialogPaymentSuccessFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onStart() {
        super.onStart();

    }




}
