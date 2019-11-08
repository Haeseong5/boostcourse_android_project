package com.example.a82102.movieprojectfinal.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.a82102.movieprojectfinal.Data.CommentData;
import com.example.a82102.movieprojectfinal.Data.CommentListResult;
import com.example.a82102.movieprojectfinal.Adapter.AllViewAdapter;
import com.example.a82102.movieprojectfinal.Helper.AppHelper;
import com.example.a82102.movieprojectfinal.Item.ReviewItem;
import com.example.a82102.movieprojectfinal.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.a82102.movieprojectfinal.Activity.DetailActivity.REVIEW_WRITE_CODE;
import static com.example.a82102.movieprojectfinal.Activity.DetailActivity.profileURL;

public class AllVIewActivity extends AppCompatActivity{
    public static Context context_allview;
    ListView allViewListView;
    ArrayList<ReviewItem> reviewList = new ArrayList<>();
    AllViewAdapter allViewAdapter;
    TextView btWrite;
    ImageView ivBackButton;
    ImageView ivGrade;
    TextView tvTitle;
    RatingBar ratingBar;
    Toolbar toolbar;
    int id;
    String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList";

    String movieName;
    int grade;
    float rating;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_allview);
        context_allview = this;
        toolbar = findViewById(R.id.allview_toolbar);
        allViewListView = findViewById(R.id.allview_listview);
        btWrite = findViewById(R.id.allview_write);
        ivBackButton = findViewById(R.id.allview_backButton);
        ivGrade = findViewById(R.id.allview_grade);
        tvTitle = findViewById(R.id.allview_title);
        ratingBar = findViewById(R.id.allview_rating);
        if(getIntent()!=null)
        {
            id = getIntent().getExtras().getInt("movieNumber");
            movieName = getIntent().getExtras().getString("movieName");
            grade = getIntent().getExtras().getInt("grade");
            rating = getIntent().getExtras().getFloat("rating");
            rating = rating/2;
            tvTitle.setText(movieName);
            ratingBar.setRating(rating);
            switch (grade)
            {
                case 12:
                    ivGrade.setImageResource(R.drawable.ic_12);
                    break;
                case 15:
                    ivGrade.setImageResource(R.drawable.ic_15);
                    break;
                case 19:
                    ivGrade.setImageResource(R.drawable.ic_19);
                    break;
            }
        }

        allViewAdapter = new AllViewAdapter(this,reviewList);
        commentListRequest(id, url);
        allViewListView.setAdapter(allViewAdapter);

        btWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllVIewActivity.this, WriteActivity.class);
                intent.putExtra("movieNumber",id);
                intent.putExtra("movieName",movieName);
                intent.putExtra("grade",grade);
                startActivityForResult(intent,REVIEW_WRITE_CODE);
            }
        });
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    protected void commentListRequest(final int id, String url)
    {
        //53866026ec1951a1f4625cf579166162 key
        Log.d("url",url);
        StringRequest request = new StringRequest(
                Request.Method.POST, //GET방식으로 요청
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //응답객체를 받아온다
                        getCommentList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                최근 조회 숫자 : 최근 O개 조회 all : 전체 조회
                Map<String,String> params = new HashMap<String,String>();
                String movieId = Integer.toString(id);
                params.put("id", movieId);
                params.put("limit","5"); //최근10개 조회
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }//end movieListRequest.
    public void getCommentList(String response)
    {
        Gson gson = new Gson();
        CommentListResult commentListResult = gson.fromJson(response, CommentListResult.class);
        if(commentListResult != null)
        {
            //gson데이터 받아와서 처리;
            ArrayList<CommentData> commentList = commentListResult.result; //movieResult클래스에 잇는 리스트를 대입시킨다.
            Log.d("message: ", commentListResult.message);
            Log.d("code: ", String.valueOf(commentListResult.code));
            Log.d("result size",Integer.toString(commentList.size()));

            if(commentListResult.code==200)
            {
                String timeStamp;
                String recommendNumber;
                for(int i=0; i<commentList.size(); i++)
                {
                    CommentData commentData = commentList.get(i);
                    timeStamp = Integer.toString(Integer.parseInt(commentData.timestamp));
                    recommendNumber = Integer.toString(commentData.recommend);
                    float rating = commentData.rating/2;
                    reviewList.add(new ReviewItem(commentData.writer_image,commentData.writer,timeStamp,commentData.contents,recommendNumber,rating));
                }
                Log.d("reviewList Size",Integer.toString(commentList.size()));
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REVIEW_WRITE_CODE:  // 리뷰 작성 후 돌아왔을 때 실행
                    String reviewContent = data.getStringExtra("review");
                    String time = data.getStringExtra("time");
                    String writer = data.getStringExtra("writer");
                    float rating = data.getFloatExtra("rating",0);
                    reviewList.add(new ReviewItem(profileURL,writer,time,reviewContent,"1",rating));
                    Log.d("작성 후 돌아왔을 때 all", time);
                    Log.d("작성 후 돌아왔을 때 all", writer);

                    allViewAdapter.notifyDataSetChanged();

                    //리스트뷰에 추가
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        allViewAdapter.notifyDataSetChanged();
    }
}
