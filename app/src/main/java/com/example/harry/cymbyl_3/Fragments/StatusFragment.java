package com.example.harry.cymbyl_3.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.harry.cymbyl_3.MainActivity;
import com.example.harry.cymbyl_3.R;

import java.util.ArrayList;

public class StatusFragment extends Fragment {
    private Button reset;
    private TextView message, days;
    ArrayList<Integer> feq;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        reset = view.findViewById(R.id.restart);
        message = view.findViewById(R.id.num_message);
        days = view.findViewById(R.id.num_days);

        feq = ((MainActivity)getActivity()).load();

        days.setText(Integer.toString(dayCalculator()));
        message.setText(Integer.toString(feq.get(0)));
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFragment(2);
            }
        });


        return view;
    }
    private int dayCalculator(){
        int endH = feq.get(4);
        if (endH<feq.get(2)){
            endH+=24;
        }
        if(feq.get(1)==0){
            return 0;
        }
        int n = (int)Math.floor( ( (endH-feq.get(2))*60 + feq.get(5)-feq.get(3))/ feq.get(1));
        if(n==0){
            return 0;
        }
        return (int)Math.ceil(feq.get(0)/n);
    }
}
