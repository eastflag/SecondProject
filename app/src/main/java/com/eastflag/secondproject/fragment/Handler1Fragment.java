package com.eastflag.secondproject.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eastflag.secondproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Handler1Fragment extends Fragment {


    public Handler1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_handler1, container, false);
    }

}