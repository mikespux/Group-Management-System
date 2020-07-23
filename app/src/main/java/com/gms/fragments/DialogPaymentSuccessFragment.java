package com.gms.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.gms.R;
import com.gms.database.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DialogPaymentSuccessFragment extends DialogFragment {

    private View root_view;
    SharedPreferences prefs;
    TextView txtDate,txtTime,txtName,txtEmail,txtAmount;
    ImageView card_logo;
    TextView card_type,card_details;
    DBHelper dbhelper;
    SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.dialog_payment_success, container, false);

        ((FloatingActionButton) root_view.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        initializer();
        return root_view;
    }
    public void initializer(){
        dbhelper = new DBHelper(getActivity());
        db= dbhelper.getReadableDatabase();
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        txtDate=root_view.findViewById(R.id.txtDate);
        txtTime=root_view.findViewById(R.id.txtTime);
        txtName=root_view.findViewById(R.id.txtName);
        txtEmail=root_view.findViewById(R.id.txtEmail);
        txtAmount=root_view.findViewById(R.id.txtAmount);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm aa");

        txtDate.setText(format.format(cal.getTime()));
        txtTime.setText(format1.format(cal.getTime()));

        Cursor d = dbhelper.getGivenName(prefs.getString("user", ""));
        String user_fullname = d.getString(1);
        txtName.setText(user_fullname);
        txtAmount.setText(prefs.getString("et_amount", ""));

        card_logo=root_view.findViewById(R.id.card_logo);
        card_type=root_view.findViewById(R.id.card_type);
        card_details=root_view.findViewById(R.id.card_details);
        if(prefs.getString("type", "").equals("1")){
            card_logo.setImageResource(R.drawable.ic_mpesa);
            card_type.setText("M-PESA");
            card_details.setText("****");
        }else{

            card_logo.setImageResource(R.drawable.ic_visa_new);
            card_type.setText("CARD");
            card_details.setText("*****");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}