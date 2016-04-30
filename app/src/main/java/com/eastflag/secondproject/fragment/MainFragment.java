package com.eastflag.secondproject.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eastflag.secondproject.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.button100) void click() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new Handler1Fragment())
                .addToBackStack(null) //기존프래그먼트(메인)를 스택영역에 저장.
                .commit();
    }

    @OnClick(R.id.button101) void click2() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new Handler2Fragment())
                .addToBackStack(null) //기존프래그먼트(메인)를 스택영역에 저장.
                .commit();
    }

    @OnClick(R.id.button201) void click3() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new Image1Fragment())
                .addToBackStack(null) //기존프래그먼트(메인)를 스택영역에 저장.
                .commit();
    }

    @OnClick(R.id.button301) void click301() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new BookFragment())
                .addToBackStack(null) //기존프래그먼트(메인)를 스택영역에 저장.
                .commit();
    }

    @OnClick(R.id.button401) void click401() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SchetchFragment())
                .addToBackStack(null) //기존프래그먼트(메인)를 스택영역에 저장.
                .commit();
    }

    @OnClick(R.id.button501) void click501() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new PlaceFragment())
                .addToBackStack(null) //기존프래그먼트(메인)를 스택영역에 저장.
                .commit();
    }
}
