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
 * A simple {@link Fragment} subclass.
 */
public class Handler1Fragment extends Fragment {
    private int mCount;
    @Bind(R.id.tvDisplay) TextView tvDisplay;
    private MyThread mThread;

    public Handler1Fragment() {
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
        //2. 쓰레드 시작
        mThread = new MyThread();
        mThread.start();
    }
    @OnClick(R.id.btnStop)
    public void btnStop() {
        //3. 쓰레드 종료 : run 메서드가 실행이 끝나면 종료
        mThread.setmIsRunning(false);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    //화면에 mCount dispay
                    tvDisplay.setText(String.valueOf(mCount));
                    break;
            }
        }
    };

    //1. 쓰레드 생성 (상속)
    class MyThread extends Thread {
        private boolean mIsRunning = true;
        @Override
        public void run() {
            while (mIsRunning) {
                //1초쉰 후에 1증가
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                ++mCount;

                mHandler.sendEmptyMessage(0);
            }
        }
        public void setmIsRunning(boolean isRunning) {
            mIsRunning = isRunning;
        }
    }
}
