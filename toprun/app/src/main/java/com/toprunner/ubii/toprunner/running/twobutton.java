package com.toprunner.ubii.toprunner.running;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toprunner.ubii.toprunner.R;

/**
 * Created by ly on 2016/5/20.
 */
public class twobutton extends Fragment {
    View wancheng,jixu;
    onTestListener callback = new onTestListener() {
        @Override
        public void onTest() {

        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.twobutton, container, false);
        wancheng = view.findViewById(R.id.wanchengbutton);
        jixu = view.findViewById(R.id.jixubutton);

        jixu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        System.out.println(getActivity().getFragmentManager().getBackStackEntryCount() + "个站点");
                        getFragmentManager().popBackStack();
                        callback.onTest();

            }
        });
        wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
public interface onTestListener
{
     void onTest();
}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {

            callback = (onTestListener)activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
