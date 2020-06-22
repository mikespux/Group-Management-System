package com.gms.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.gms.R;
import com.gms.activities.CalendarActivity;
import com.gms.activities.ContributionsActivity;
import com.gms.activities.MainActivity;
import com.gms.activities.MinutesActivity;

public class HomeFragment extends Fragment {
    Intent mIntent;
    View root;
    LinearLayout LtMinutes,LtContributions,LtVoting,LtMembers,LtCalendar,LtNotifications;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

      root = inflater.inflate(R.layout.fragment_home, container, false);
      initialize();
      return root;
    }

    public void initialize(){

        LtMinutes=root.findViewById(R.id.LtMinutes);
        LtContributions=root.findViewById(R.id.LtContributions);
        LtVoting=root.findViewById(R.id.LtVoting);
        LtMembers=root.findViewById(R.id.LtMembers);
        LtCalendar=root.findViewById(R.id.LtCalendar);
        LtNotifications=root.findViewById(R.id.LtNotifications);


        LtMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent= new Intent(getActivity(), MinutesActivity.class);
                startActivity(mIntent);
            }
        });

        LtContributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent= new Intent(getActivity(), ContributionsActivity.class);
                startActivity(mIntent);
            }
        });


        LtCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent= new Intent(getActivity(), CalendarActivity.class);
                startActivity(mIntent);
            }
        });
    }
}
