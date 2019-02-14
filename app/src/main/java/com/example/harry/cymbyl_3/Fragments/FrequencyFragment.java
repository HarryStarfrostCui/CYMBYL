package com.example.harry.cymbyl_3.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harry.cymbyl_3.MainActivity;
import com.example.harry.cymbyl_3.R;

import java.lang.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FrequencyFragment extends Fragment {

    private EditText mInterval;
    private EditText mStartHour;
    private EditText mEndHour;
    private EditText mStartMinute;
    private EditText mEndMinute;
    private EditText mTotal;
    private TextView mDays;
    private ImageView mNext;

    private int startHour, endHour, startMinute, endMinute, gap, total;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frequency, container, false);

        mInterval = view.findViewById(R.id.frequency_interval);
        mStartHour = view.findViewById(R.id.frequency_start_hour);
        mStartMinute = view.findViewById(R.id.frequency_start_minute);
        mEndHour = view.findViewById(R.id.frequency_end_hour);
        mEndMinute = view.findViewById(R.id.frequency_end_minute);
        mTotal = view.findViewById(R.id.frequency_total);
        mDays = view.findViewById(R.id.frequency_days);
        mNext = view.findViewById(R.id.frequency_next);

        startHour = 0;
        endHour = 23;
        startMinute = 0;
        endMinute=59;
        gap = 0;
        total = 0;

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gap == 0 && total != 0) {
                    //Toast.makeText(getContext(), Integer.toString(gap)+" "+Integer.toString(total), Toast.LENGTH_SHORT).show();
                    mInterval.setError("Gap can't be 0");
                    mInterval.requestFocus();
                    return;
                }
                ArrayList<Integer> feq = new ArrayList<>();
                feq.add(total+1);               //0
                feq.add(gap);                   //1
                feq.add(startHour);             //2
                feq.add(startMinute);           //3
                feq.add(endHour);               //4
                feq.add(endMinute);             //5
                feq.add(getCurrentTime()[0]);   //6  current hour
                feq.add(getCurrentTime()[1]);   //7  current minute

                ((MainActivity)getActivity()).save(feq);
                ((MainActivity)getActivity()).changeFragment(4);
            }
        });

        update();

        return view;
    }


    public static int[] getCurrentTime() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        int[] currentTime = {Integer.valueOf(formattedDate.substring(0,2)),
                Integer.valueOf(formattedDate.substring(3,5))};

        return currentTime;
    }

    public void update(){
        if(!mStartHour.getText().toString().isEmpty()) {
            startHour = Integer.valueOf(mStartHour.getText().toString());
            if (startHour >= 24) {
                startHour = startHour % 24;
                mStartHour.setText(Integer.toString(startHour));
            }
        }

        if(!mEndHour.getText().toString().isEmpty()) {
            endHour = Integer.valueOf(mEndHour.getText().toString());
            if (endHour >= 24) {
                endHour = endHour % 24;
                mEndHour.setText(Integer.toString(endHour));
            }
        }

        if(!mStartMinute.getText().toString().isEmpty()) {
            startMinute = Integer.valueOf(mStartMinute.getText().toString());
            if (startMinute >= 60) {
                startMinute = startMinute % 60;
                mStartMinute.setText(Integer.toString(startMinute));
            }
        }

        if(!mEndMinute.getText().toString().isEmpty()) {
            endMinute = Integer.valueOf(mEndMinute.getText().toString());
            if (endMinute >= 60) {
                endMinute = endMinute % 60;
                mEndMinute.setText(Integer.toString(endMinute));
            }
        }
        if(!mInterval.getText().toString().isEmpty()) {
            gap = Integer.valueOf(mInterval.getText().toString());
        }
        if (!mTotal.getText().toString().isEmpty()) {
            total = Integer.valueOf(mTotal.getText().toString());
        }
        mDays.setText(Integer.toString(dayCalculator()));
        refresh(1000);
    }

    private int dayCalculator(){
        int endH = endHour;
        if (endH<startHour){
            endH+=24;
        }
        if(gap==0){
            return 0;
        }
        int n = (int)Math.floor( ( (endH-startHour)*60 + endMinute-startMinute)/ gap );
        if(n==0){
            return 0;
        }
        return (int)Math.ceil(total/n);
    }

    private void refresh(int milisec){
        final Handler h = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                update();
            }
        };
        h.postDelayed(runnable, milisec);
    }



}
