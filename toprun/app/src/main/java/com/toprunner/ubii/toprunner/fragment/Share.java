package com.toprunner.ubii.toprunner.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;

import java.text.SimpleDateFormat;

public class Share extends Fragment {

    TextView time,run_time,run_speed,run_distance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_share, container, false);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");

        String strTime = timeFormat.format(new java.util.Date());
        String strDate = dateFormat.format(new java.util.Date());

        time = (TextView) view.findViewById(R.id.textView25);
        time.setText(strDate+strTime+"");

        run_time = (TextView) view.findViewById(R.id.textView22);
        run_speed = (TextView) view.findViewById(R.id.run_speed);
        run_distance = (TextView) view.findViewById(R.id.run_distance);

        Bundle bundle = getArguments();
        run_time.setText(bundle.getString("run_time"));

        return  view;
    }


}
