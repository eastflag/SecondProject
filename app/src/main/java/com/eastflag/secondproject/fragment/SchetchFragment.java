package com.eastflag.secondproject.fragment;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eastflag.secondproject.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchetchFragment extends Fragment {
    @Bind(R.id.container) LinearLayout container;

    public SchetchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schetch, container, false);
        ButterKnife.bind(this, view);

        //커스텀뷰를 동적으로 레이아웃에 추가
        //1) 인스턴스 생성
        MyView myView = new MyView(getActivity());
        //2) 뷰의 속성, 배치 속성 설정
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //3) 부모뷰에 붙이기
        container.addView(myView, params);

        return view;
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
