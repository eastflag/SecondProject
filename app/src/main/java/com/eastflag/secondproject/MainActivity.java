package com.eastflag.secondproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eastflag.secondproject.fragment.MainFragment;

public class MainActivity extends Activity {

    private FragmentManager mFm;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mMenuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getActionBar().hide();
        //메인 프래그먼트 삽입
        //프래그먼트 관리자를 이용해서 add, remove, replace 등의 트랜잭션 작업을 수행
        //트랜잭션 시작 -> add, remove, replace 등의 작업 -> commit
        mFm = getFragmentManager();
        mFm.beginTransaction()
                .replace(R.id.container, new MainFragment())
                .commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //ListView의 항목 구성
        mMenuList = getResources().getStringArray(R.array.menu_array); //Model
        mDrawerList = (ListView) findViewById(R.id.left_drawer);//View
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.drawer_list_item, mMenuList); //Control
        mDrawerList.setAdapter(adapter); //ListView에 어댑터연결

        //메뉴버튼 클릭시 슬라이딩 메뉴를 토글글
       findViewById(R.id.ivTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if(mFm.getBackStackEntryCount() > 0) {
            mFm.popBackStack();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Smart School")
                    .setMessage("종료하시겠습니까?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

}
