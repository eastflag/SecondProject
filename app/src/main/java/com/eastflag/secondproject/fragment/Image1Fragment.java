package com.eastflag.secondproject.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eastflag.secondproject.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Image1Fragment extends Fragment {
    public static final String IMG_URL = "http://movie.phinf.naver.net/20131107_114/1383801279704yKICY_JPEG/movie_image.jpg";
    @Bind(R.id.ivImg)
    ImageView ivImg;
    private ProgressDialog mProgressDialog;

    public Image1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image1, container, false);
        ButterKnife.bind(this, view);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Wait");
        mProgressDialog.setMessage("getting image...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return view;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.hide();
            Bitmap bitmap = (Bitmap) msg.obj;
            ivImg.setImageBitmap(bitmap);
        }
    };

    @OnClick(R.id.btnImg)
    public void OnClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //네트웍으로 이미지 가져오기
                    URL url = new URL(IMG_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //connection.setDoOutput(true);
                    connection.setConnectTimeout(3000);
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = bitmap;
                    mHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        mProgressDialog.show();
    }
}
