package com.example.a82102.movieprojectfinal.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a82102.movieprojectfinal.Data.CommentData;
import com.example.a82102.movieprojectfinal.Data.CommentListResult;
import com.example.a82102.movieprojectfinal.Data.LikeData;
import com.example.a82102.movieprojectfinal.Data.LikeResult;
import com.example.a82102.movieprojectfinal.Data.jMovie;
import com.example.a82102.movieprojectfinal.Data.jMovieResult;
import com.example.a82102.movieprojectfinal.Helper.AppHelper;
import com.example.a82102.movieprojectfinal.R;
import com.example.a82102.movieprojectfinal.Adapter.ReviewAdapter;
import com.example.a82102.movieprojectfinal.Item.ReviewItem;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    public static final int REVIEW_WRITE_CODE = 100;
    public static final int REVIEW_LIST_CODE = 200;
    public static String profileURL = "https://st3.depositphotos.com/1767687/17621/v/1600/depositphotos_176214034-stock-illustration-default-avatar-profile-icon.jpg";
    String movieURL = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie";
    String commentURL = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList";
    ScrollView scrollView;
    ReviewAdapter adapter;
    ArrayList<ReviewItem> reviewList = new ArrayList<>();
    ImageView poster;
    TextView title;
    ImageView grade;
    TextView date;
    TextView genre;
    TextView runningTime;
    ImageView like;
    ImageView unlike;
    TextView likeCount;
    TextView unlikeCount;
    TextView rating;
    TextView reservation;
    RatingBar ratingBar;
    TextView ratingScore;
    TextView customerCount;
    TextView content; //줄거리
    TextView director; //감독
    TextView act;
    ListView listView_review;
    Button viewAll;//모두보기
    Button write;
    ImageButton kakao;
    LoginButton facebook_login;
    LikeData likeData;
    int position;
    int updateLikeCount;
    int updateDislikeCount;
    private static final String TAG = "MainActivity";
    private CallbackManager callbackManager;
    int ageGrade;
    float user_rating;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        poster = findViewById(R.id.poster);
        title = findViewById(R.id.title);
        grade= findViewById(R.id.grade);
        date= findViewById(R.id.date);
        genre= findViewById(R.id.genre);
        runningTime= findViewById(R.id.runningTime);
        like= findViewById(R.id.bt_like);
        unlike= findViewById(R.id.bt_unlike);
        likeCount= findViewById(R.id.like_count);
        unlikeCount= findViewById(R.id.unlike_count);
        rating= findViewById(R.id.ratingScore);//예매율
        reservation = findViewById(R.id.reservationInfo);
        ratingBar = findViewById(R.id.ratingBar);
        ratingScore= findViewById(R.id.ratingScore);
        customerCount= findViewById(R.id.customerCount);
        content= findViewById(R.id.content );//줄거리
        director= findViewById(R.id.director); //감독
        act= findViewById(R.id.act);
        viewAll= findViewById(R.id.viewAll);
        write= findViewById(R.id.write);
        kakao= findViewById(R.id.kakao);
        scrollView=findViewById(R.id.scrollview);
        listView_review = findViewById(R.id.listview_review);
        ratingBar.setIsIndicator(true); //레이팅바의 별을 변경 불가능하게 만든다.
        initLayout();
        adapter = new ReviewAdapter(getApplicationContext(),reviewList) ;
        likeData = new LikeData();

        Intent intent = getIntent();
        if(intent!=null)
        {
            position = intent.getExtras().getInt("position")+1;
            Log.d("getIntent position",Integer.toString(position));
        }
        if(AppHelper.requestQueue == null)
        {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        movieDetailRequest(position, movieURL);
        commentListRequest(position, commentURL);
        adapter.notifyDataSetChanged();
        setButton();

    }//end onCreate

    private void initLayout() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setTitle("영화 상세");
        actionBar.setDisplayHomeAsUpEnabled(true);// 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);//뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
//
//        drawerLayout =  findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nv_main_navigation_root);
//        drawerToggle = new ActionBarDrawerToggle(
//                this,
//                drawerLayout,
//                toolbar,
//                R.string.drawer_open,
//                R.string.drawer_close
//        );
//        drawerLayout.addDrawerListener(drawerToggle);
//        navigationView.setNavigationItemSelectedListener(this);

    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.item1:
//                Toast.makeText(this, "item1 clicked..", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.item2:
//                Toast.makeText(this, "item2 clicked..", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.item3:
//                Toast.makeText(this, "item3 clicked..", Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return false;
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        drawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Pass the event to ActionBarDrawerToggle, if it returns
//        // true, then it has handled the app icon touch event
//        if (drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        // Handle your other action bar items...
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    protected void setButton()
    {
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, WriteActivity.class);
                intent.putExtra("movieNumber",position);
                intent.putExtra("movieName",title.getText().toString());
                intent.putExtra("grade",ageGrade);

                startActivityForResult(intent, REVIEW_WRITE_CODE);
            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, AllVIewActivity.class);
                intent.putExtra("movieNumber",position);
                intent.putExtra("movieName",title.getText().toString());
                intent.putExtra("grade",ageGrade);
                intent.putExtra("rating",user_rating);

                startActivityForResult(intent,REVIEW_LIST_CODE);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppHelper.requestQueue == null)
                {
                    AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
                }
                updateLikeCount = Integer.parseInt(likeCount.getText().toString());
                updateDislikeCount = Integer.parseInt(unlikeCount.getText().toString());

                if(likeData.getLikeStatus() == 0) //좋아요를 누르지 않은 상태
                {
                    likeData.setLikeyn("Y");
                    likeData.setLikeStatus(1); //좋아요 이미 했음 상태로 바꿔줌
                    like.setImageResource(R.drawable.ic_thumb_up_selected);
                    updateLikeCount++;
                    likeCount.setText(Integer.toString(updateLikeCount));
                    if(likeData.getDislikeStatus()>0) //싫어요가 눌린 상태라면
                    {                                 //좋아요를 눌렀을 때 싫어요가 내려가야함.
                        likeData.setDislikeyn("N");
                        likeData.setDislikeStatus(0);
                        unlike.setImageResource(R.drawable.ic_thumb_down);
                        updateDislikeCount--;
                        unlikeCount.setText(Integer.toString(updateDislikeCount));
                    }
                }else if(likeData.getLikeStatus()>0) //좋아요를 누른 상태
                {
                    likeData.setLikeyn("N"); //좋아요 취소
                    likeData.setLikeStatus(0); //좋아요 안 한 상태로 바꿔줌.
                    like.setImageResource(R.drawable.ic_thumb_up);
                    updateLikeCount--;
                    likeCount.setText(Integer.toString(updateLikeCount));
                }
                increaseLikeDisLikeRequest(likeData.getLikeyn(),likeData.getDislikeyn());
            }
        });
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppHelper.requestQueue == null)
                {
                    AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
                }
                if(likeData.getDislikeStatus() == 0)
                {
                    likeData.setDislikeyn("Y");
                    likeData.setDislikeStatus(1);
                    unlike.setImageResource(R.drawable.ic_thumb_down_selected);
                    updateDislikeCount++;
                    unlikeCount.setText(Integer.toString(updateDislikeCount));
                    if(likeData.getLikeStatus()>0) //싫어요를 눌렀을 때 좋아요가 내려가야함.
                    {
                        likeData.setLikeyn("N");
                        likeData.setLikeStatus(0);
                        like.setImageResource(R.drawable.ic_thumb_up);
                        updateLikeCount--;
                        likeCount.setText(Integer.toString(updateLikeCount));
                    }
                }else if(likeData.getDislikeStatus()>0)
                {
                    likeData.setDislikeyn("N");
                    likeData.setDislikeStatus(0);
                    unlike.setImageResource(R.drawable.ic_thumb_down);
                    updateDislikeCount--;
                    unlikeCount.setText(Integer.toString(updateDislikeCount));

                }
                increaseLikeDisLikeRequest(likeData.getLikeyn(),likeData.getDislikeyn());
            }
        });
    }

    protected void movieDetailRequest(final int id, String url)
    {
        //53866026ec1951a1f4625cf579166162 key

        Log.d("url",url);
        StringRequest request = new StringRequest(
                Request.Method.POST, //GET방식으로 요청
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //응답객체를 받아온다
                        getMovieInfo(response);
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
                Map<String,String> params = new HashMap();
                String movieId = Integer.toString(id);
                params.put("id", movieId);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }//end movieListRequest.

    protected void commentListRequest(final int id, String url)
    {
        Log.d("url",url+id);
        StringRequest request = new StringRequest(
                Request.Method.POST,
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
                params.put("limit","3"); //최근 n개 조회
                Log.d("param ", "p");
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }//end movieListRequest.
    protected void getMovieInfo(String response) //응답결과를 처리하는 메소드
    {
        Gson gson = new Gson();
        jMovieResult movieResult = gson.fromJson(response, jMovieResult.class);
        if(movieResult != null)
        {
            //gson데이터 받아와서 처리;
            ArrayList<jMovie> result = movieResult.result; //movieResult클래스에 잇는 리스트를 대입시킨다.
            Log.d("message: ", movieResult.message);
            Log.d("result size",Integer.toString(result.size()));
            Log.d("position", String.valueOf(position));
            Log.d("code: ", String.valueOf(movieResult.code));

            if(movieResult.message.equals("movie readMovie 성공") )
            {

                Log.d("position", String.valueOf(position));
                jMovie movie = result.get(0);
                Log.d("title",movie.title);
                title.setText(movie.title);
                date.setText(movie.date);
                genre.setText(movie.genre+" | ");
                runningTime.setText(Integer.toString(movie.duration)+"분");
                content.setText(movie.synopsis);
                likeCount.setText(Integer.toString(movie.like));
                unlikeCount.setText(Integer.toString(movie.dislike));
                reservation.setText(Integer.toString(movie.reservation_grade)+"위 " +Float.toString(movie.reservation_rate)+"%");
                user_rating=movie.reviewer_rating;
                ratingScore.setText(Float.toString(movie.reviewer_rating));
                customerCount.setText(movie.audience);
                act.setText(movie.actor);
                director.setText(movie.director);
                float rating = movie.reviewer_rating/2;
                ratingBar.setRating(rating);
                ageGrade = movie.grade;
                switch (movie.grade)
                {
                    case 12:
                        grade.setImageResource(R.drawable.ic_12);
                        break;
                    case 15:
                        grade.setImageResource(R.drawable.ic_15);
                        break;
                    case 19:
                        grade.setImageResource(R.drawable.ic_19);
                        break;
                }
//                Glide.

                sendImageRequest(movie.thumb);
            }
        }
    }
    public void getCommentList(String response)
    {
        Gson gson = new Gson();
        CommentListResult commentListResult = gson.fromJson(response, CommentListResult.class);
        if(commentListResult != null)
        {
            //gson데이터 받아와서 처리;
            ArrayList<CommentData> commentList = commentListResult.result; //movieResult클래스에 잇는 리스트를 대입시킨다.
            Log.d("COMMENT message: ", commentListResult.message);
            Log.d("COMMENT code: ", String.valueOf(commentListResult.code));
            Log.d("COMMENT result size",Integer.toString(commentList.size()));
            Log.d("COMMENT position", String.valueOf(position));

            if(commentListResult.code==200)
            {
                String timeStamp;
                String recommendNumber;
                float rating;
                for(int i=0; i<commentList.size(); i++)
                {
                    CommentData commentData = commentList.get(i);
                    timeStamp = Integer.toString(Integer.parseInt(commentData.timestamp));
                    recommendNumber = Integer.toString(commentData.recommend);
                    rating = commentData.rating/2;
                    reviewList.add(new ReviewItem(profileURL,commentData.writer,timeStamp,commentData.contents,recommendNumber,rating));
                    listView_review.setAdapter(adapter);
                }
                Log.d("reviewList Size",Integer.toString(commentList.size()));

            }
            scrollView.smoothScrollTo(0,0); //스크롤뷰를 최상단으로 위치시킴.

        }
    }

    protected void increaseLikeDisLikeRequest(final String likeyn, final String dislikeyn)
    {
        //53866026ec1951a1f4625cf579166162 key
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/increaseLikeDisLike";
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //응답객체를 받아온다
                        processResponse(response);
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
                Map<String,String> params = new HashMap<String,String>();
                params.put("id",Integer.toString(position));
                Log.d("like id",Integer.toString(position));
                Log.d("like parms",likeyn+"  "+ dislikeyn );
                params.put("likeyn",likeyn); //좋아요,또는 좋아요 취소 likeyn = Y 이면 좋아요 + 1 N 이면 좋아요를 취소 (즉 -1)
                params.put("dislikeyn",dislikeyn); //싫어요,또는 싫어요 취소 dislikeyn = Y 이면 싫어요 + 1 N 이면 싫어요를 취소 (즉 -1)
//               * 위에 likeyn  또는 dislike 중 하나만 값이 오고, 다른 하나는 null 이 올 수 있음

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }//end movieListRequest.

    protected void processResponse(String response) //응답결과를 처리하는 메소드
    {
        Gson gson = new Gson();
        LikeResult result = gson.fromJson(response, LikeResult.class);
        if(result != null)
        {
            //gson데이터 받아와서 처리;
            Log.d("like result message ", String.valueOf(result.message));
            Log.d("like result code ", String.valueOf(result.code));

            if(result.code == 200) //좋아요 요청 성공 시 .
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         finish();
            }
        });
            }else
            {
                Toast.makeText(getApplicationContext(), "좋아요 실패 ㅠ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void sendImageRequest(String url)
    {
        ImageLoadTask task = new ImageLoadTask(url, poster);
        task.execute();
    }

    //onStart() 메서드 전·후로 실행 된다.
    //onResume() 메서드 전에 꼭 실행 된다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REVIEW_WRITE_CODE:
                    String reviewContent = data.getStringExtra("review");
                    String time = data.getStringExtra("time");
                    float rating = data.getFloatExtra("rating",0);
                    String writer = data.getStringExtra("writer");
                    reviewList.add(new ReviewItem(profileURL,writer,time,reviewContent,"1",rating));
                    Log.d("작성 후 돌아왔을 때 detail", time);
                    Log.d("작성 후 돌아왔을 때 detail", writer);

                    adapter.notifyDataSetChanged();
                    //리스트뷰에 추가
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

        Log.d("onResume detail","onResume");
    }
//
//
//
//    protected String getFacebookId()
//    {
//        final String[] str = new String[1];
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        GraphRequest request = GraphRequest.newMeRequest(accessToken,
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(
//                            JSONObject object,
//                            GraphResponse response) {
//                        try {
//                            Log.d("facebook id", object.getString("name"));
//                            str[0] = object.getString("name");
//                        } catch (JSONException je) {
//                            Log.e("FB", "No key provided.");
//                        }
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,link");
//        request.setParameters(parameters);
//        request.executeAsync();
//        Log.d("str", String.valueOf(str));
//        return str[0];
//    }
//    void facebookLogin()
//    {
//        //facebook
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//        callbackManager = CallbackManager.Factory.create();
//
//        facebook_login = findViewById(R.id.facebook_login);
//        facebook_login.setReadPermissions("email");
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                        Log.d(TAG, "onSucces LoginResult=" + loginResult);
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        Log.d(TAG, "onCancel");
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Log.d(TAG, "onError");
//                    }
//                });
//    }
}
