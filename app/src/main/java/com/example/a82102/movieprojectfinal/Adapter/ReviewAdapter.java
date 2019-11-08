package com.example.a82102.movieprojectfinal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a82102.movieprojectfinal.Activity.ImageLoadTask;
import com.example.a82102.movieprojectfinal.R;
import com.example.a82102.movieprojectfinal.Item.ReviewItem;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<ReviewItem> reviewList = new ArrayList<>();
    ImageView profile;

    public ReviewAdapter (Context context, ArrayList<ReviewItem> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_review, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView id = convertView.findViewById(R.id.id);
        TextView time = convertView.findViewById(R.id.time);
        TextView review = convertView.findViewById(R.id.review);
        TextView recommend = convertView.findViewById(R.id.recommand);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);
        profile = convertView.findViewById(R.id.profile);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ReviewItem reviewItem = reviewList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        id.setText(reviewItem.getId());
        time.setText(reviewItem.getTime());
        review.setText(reviewItem.getReview());
        recommend.setText(reviewItem.getReview());
        ratingBar.setRating(reviewItem.getRating());
        sendImageRequest(reviewItem.getProfile());

        return convertView;
    }



    public void sendImageRequest(String url)
    {
        ImageLoadTask task = new ImageLoadTask(url, profile);
        task.execute();
    }
}
