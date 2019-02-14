package com.example.harry.cymbyl_3.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.harry.cymbyl_3.MainActivity;
import com.example.harry.cymbyl_3.R;

public class InputFragment extends Fragment {

    private EditText mMantra;
    private ImageView mNextButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        mNextButton = view.findViewById(R.id.input_nextBTN);
        mMantra = view.findViewById(R.id.input_editText);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mantra = mMantra.getText().toString();
                if(mantra.length()>150){
                    mMantra.setError("Exceeded Maximum Mantra Length: by "
                            + Integer.toString(mantra.length()-150));
                    mMantra.requestFocus();
                }else {
                    ((MainActivity)getActivity()).setMessage(mantra);
                    ((MainActivity)getActivity()).changeFragment(3);
                }
            }
        });


        return view;
    }
}
