package com.eastflag.secondproject.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eastflag.secondproject.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchetchFragment extends Fragment {
    @Bind(R.id.container) LinearLayout container;
    @Bind(R.id.tvPenSize) TextView tvPenSize;
    @Bind(R.id.tvPenColor) TextView tvPenColor;
    private int mProgress = 5; //메인화면의 펜사이즈 값
    private int mColor = Color.BLACK; // 메인화면의 색깔

    //1. 터치시에 터치 정보를 객체로 저장
    //2. 저장된 객체로 부터 값을 꺼내서 커스텀뷰에 그리기
    public SchetchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schetch, container, false);
        ButterKnife.bind(this, view);

        tvPenSize.setText(String.valueOf(mProgress));
        tvPenColor.setBackgroundColor(mColor);

        //커스텀뷰를 동적으로 레이아웃에 추가
        //1) 인스턴스 생성
        MyView myView = new MyView(getActivity());
        //2) 뷰의 속성, 배치 속성 설정
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //3) 부모뷰에 붙이기
        container.addView(myView, params);

        //커스텀뷰에 터치 리스너 구현
        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("LDK", "type: " + event.getAction() +
                        ", x: " + event.getX() +
                        ", y: " + event.getY());
                return true; //false일경우 다운이벤트만 받고 아래로 넘겨준다.
            }
        });

        return view;
    }

    @OnClick(R.id.btnPenSize)
    void btnPenSize() {
        View view = View.inflate(getActivity(), R.layout.popup_pensize, null);
        //1. 메인의 값(mProgress)을 읽어서 대화창의 seekBar와 텍스트뷰에 표현하기
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
        final TextView popupPenSize = (TextView) view.findViewById(R.id.popupPenSize);
        seekBar.setProgress(mProgress);
        popupPenSize.setText(String.valueOf(mProgress));

        //2. 시크바 리스너 구현하기
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                popupPenSize.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("펜사이즈")
                //.setMessage("메시지내용"), 메시지를 커스터마이징하는 setView가 제공.
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //3. ok버튼 클릭시 메인에 값 반영하기
                        mProgress = seekBar.getProgress();
                        tvPenSize.setText(String.valueOf(mProgress));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @OnClick(R.id.btnPenColor) void btnPenColor() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), mColor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mColor = color;
                tvPenColor.setBackgroundColor(mColor);
            }
        });

        dialog.show();
    }

    class MyView extends View {
        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //백그라운드를 회색으로 칠하기
            canvas.drawColor(Color.LTGRAY);
            //객체정보를 가져와서 여기에서 그림을 그린다.

            super.onDraw(canvas);
        }
    }
}
