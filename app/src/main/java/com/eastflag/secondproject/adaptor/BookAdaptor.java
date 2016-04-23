package com.eastflag.secondproject.adaptor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
            convertView.setTag(holder);
        } else { //스크롤로 인해 더이상 사용되지 않는 뷰를 리스트뷰가 담아서 준다.
            convertView = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        //그래서 findviewById 매번 하지 않고 ViewHolder 기법으로 저장했다가 재사용
        //=>ViewHolder 패턴
        holder.tvTitle.setText(mBookList.get(position).getTitle());

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
    }
}
