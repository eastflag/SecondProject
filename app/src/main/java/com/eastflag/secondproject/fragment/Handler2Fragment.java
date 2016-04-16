package com.eastflag.secondproject.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eastflag.secondproject.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 타이머 구현을 쓰레드 없이 핸들러만 이용해서 구현하기
 */
public class Handler2Fragment extends Fragment {
    private int mCount;
    @Bind(R.id.tvDisplay) TextView tvDisplay;

    public Handler2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handler1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnStart)
    public void btnStart() {
        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = mCount;
        mHandler.sendMessageDelayed(msg, 1000);
    }
    @OnClick(R.id.btnStop)
    public void btnStop() {
        mHandler.removeMessages(0);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ++mCount;
            tvDisplay.setText(String.valueOf(mCount));

            Message msg2 = mHandler.obtainMessage();
            msg2.what = 0;
            msg2.arg1 = mCount;
            mHandler.sendMessageDelayed(msg2, 1000);

        }
    };
}
