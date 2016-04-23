package com.eastflag.secondproject.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eastflag.secondproject.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    private AQuery mAq;

    public Image1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image1, container, false);
        ButterKnife.bind(this, view);
        mAq = new AQuery(view);

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

    @OnClick(R.id.btnImg2)
    public void OnClick2() {
        //AQuery 로 이미지 가져오기
        //mAq.id(R.id.ivImg).image(IMG_URL).progress(mProgressDialog);
        mAq.ajax(IMG_URL, InputStream.class, new AjaxCallback<InputStream>(){
            @Override
            public void callback(String url, InputStream object, AjaxStatus status) {
                mProgressDialog.hide();
                Bitmap bitmap = BitmapFactory.decodeStream(object);
                ivImg.setImageBitmap(bitmap);
            }
        });
        mProgressDialog.show();
    }

    @OnClick(R.id.btnSend) void sendImg() {
        //서버에 이미지를 multi-part 프로토콜로 업로드
        String url = "http://www.javabrain.kr:8080/image/upload";

        Map<String, Object> params = new HashMap<String, Object>();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)ivImg.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        params.put("file1", byteArray);

        mAq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            public void callback(String url, JSONObject object, AjaxStatus status) {
                Log.d("LDK", object.toString());
            }
        });
    }


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
