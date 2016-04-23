package com.eastflag.secondproject.adaptor;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.eastflag.secondproject.R;
import com.eastflag.secondproject.domain.BookVO;

import java.util.ArrayList;

/**
 * 어댑터의 4가지 함수는 누가 호출하느냐? 리스트뷰
 * getCount : 모델의 전체 갯수
 * getView : n번째 해당하는 View를 리턴하는 함수
 */
public class BookAdaptor extends BaseAdapter {
    private Context mContext;
    private ArrayList<BookVO> mBookList;

    public BookAdaptor(Context context, ArrayList<BookVO> booklist) {
        mContext = context;
        mBookList =booklist;
    }

    @Override
    public int getCount() {
        return mBookList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //두번째 파라메터 convertView는 리스트뷰가 재활용하기 위한 항목
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) { //리스트뷰를 최초에 생성할때
            convertView = View.inflate(mContext, R.layout.item_book, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            holder.ivImg = (ImageView) convertView.findViewById(R.id.ivImg);
            convertView.setTag(holder);
        } else { //스크롤로 인해 더이상 사용되지 않는 뷰를 리스트뷰가 담아서 준다.
            convertView = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        //그래서 findviewById 매번 하지 않고 ViewHolder 기법으로 저장했다가 재사용
        //=>ViewHolder 패턴
        //TextView의 html 처리: Html.fromHtml 사용
        //TextView의 글자가 길때 자르기
        holder.tvTitle.setText(Html.fromHtml(mBookList.get(position).getTitle()));
        holder.tvAuthor.setText(mBookList.get(position).getAuthor());
        holder.tvDescription.setText(Html.fromHtml(mBookList.get(position).getDescription()));
        holder.tvPrice.setText(mBookList.get(position).getPrice());

        //이미지 보여주기
        //딜레이 로딩 이미지 : 스크롤 중일때는 이미지처리를 하지 않고,
        //스크롤이 멈추면 그때 이미지를 가져온다.
        AQuery aq = new AQuery(convertView);
        if(aq.shouldDelay(position, convertView, parent,
                mBookList.get(position).getImgUrl())){
            //현재 리스트가 스크롤일때는 이미처 처리를 하지 않는다.
        } else {
            //두번째파라메터: 메모리캐쉬, 세번째파라메터: 파일캐쉬 사용하기
            aq.id(holder.ivImg).image(mBookList.get(position).getImgUrl(), true, true);
        }


        return convertView;
    }

    class ViewHolder {
        ImageView ivImg;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvDescription;
        TextView tvPrice;
    }
}
