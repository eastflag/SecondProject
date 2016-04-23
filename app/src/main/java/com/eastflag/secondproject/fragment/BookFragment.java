package com.eastflag.secondproject.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.eastflag.secondproject.R;
import com.eastflag.secondproject.domain.BookVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment {

    private static final String BOOK_URL = "https://openapi.naver.com/v1/search/book.xml?query=%s";

    private AQuery mAq;
    private ArrayList<BookVO> mBookList = new ArrayList<BookVO>();
    @Bind(R.id.etBook) EditText etBook;

    public BookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        ButterKnife.bind(this, view);
        mAq = new AQuery(view);
        return view;
    }

    @OnClick(R.id.btnSubmit)
    void btnSearch() {
        String query = etBook.getText().toString();
        /*HashMap<String, String> params = new HashMap<String, String>();
        params.put("query", query);*/

        mAq.ajax(String.format(BOOK_URL, query), XmlDom.class, new AjaxCallback<XmlDom>() {
            @Override
            public void callback(String url, XmlDom object, AjaxStatus status) {
                Log.d("LDK", "url: " + url);
                Log.d("LDK", "status code: " + status.getCode());

                // getElementsByTagName(tag) 와 동일, 노드리스트를 리턴받는다.
                List<XmlDom> itemList =object.tags("item");
                for(XmlDom item : itemList) {
                    //title 노드를 리턴
                    XmlDom titleNode = item.tag("title");
                    String strTitle = titleNode.text(); //텍스트노드의 텍스트를 가져옴.

                    String strAuthor = item.tag("author").text();
                    String strImg = item.tag("image").text();

                    BookVO book = new BookVO();
                    book.setTitle(strTitle);
                    book.setAuthor(strAuthor);
                    book.setImgUrl(strImg);
                    book.setDescription(item.tag("description").text());
                    book.setPrice(item.tag("price").text());
                    mBookList.add(book);
               }
            }
        }.header("X-Naver-Client-Id", "7aWy98Ywds8IV1NEXUAL")
                .header("X-Naver-Client-Secret", "fa1vHElxst"));
    }
}
