package com.example.a82102.movieprojectfinal.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.a82102.movieprojectfinal.Data.WriteCommentResult;
import com.example.a82102.movieprojectfinal.Helper.AppHelper;
import com.example.a82102.movieprojectfinal.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends AppCompatActivity{
    TextView tvMovieName;
    RatingBar ratingBar;
    ImageView ivGrade;
    EditText etReview;
    Button btWrite;
    Button btFinish;
    int position;
    String movieName;
    int grade;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        etReview = findViewById(R.id.write_review);
        btWrite = findViewById(R.id.write_button);
        tvMovieName = findViewById(R.id.write_movie_title);
        ratingBar = findViewById(R.id.write_rating);
        ivGrade = findViewById(R.id.write_grade_image);
        btFinish = findViewById(R.id.write_finish_button);

        if(getIntent()!=null){
            try{
                position = getIntent().getExtras().getInt("movieNumber");
                movieName = getIntent().getExtras().getString("movieName");
                grade = getIntent().getExtras().getInt("grade");
                Log.d("getIntent", String.valueOf(position));
                tvMovieName.setText(movieName);
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
                }            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(),Float.toString(rating),Toast.LENGTH_SHORT).show();
            }
        });

        btWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etReview.getText().toString().length()<4)
                {
                    Toast.makeText(getApplicationContext(),"4자 이상 입력해주세요",Toast.LENGTH_SHORT).show();
                    etReview.getText().clear();
                }else
                {
                    sendRequest();

                }
            }
        });
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void sendRequest()
    {
        //53866026ec1951a1f4625cf579166162 key
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/createComment";
        StringRequest request = new StringRequest(
                Request.Method.POST,
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
                params.put("writer","로그인 안함");
                params.put("rating",Float.toString(ratingBar.getRating()));
                params.put("time",getDate());
                params.put("contents",etReview.getText().toString());
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }//end movieListRequest.

    protected void processResponse(String response) //응답결과를 처리하는 메소드
    {
        Gson gson = new Gson();
        WriteCommentResult result = gson.fromJson(response, WriteCommentResult.class);
        if(result != null)
        {
            //gson데이터 받아와서 처리;
            Log.d("write result status ", String.valueOf(result.status));
            Log.d("write result message ", String.valueOf(result.message));
            Log.d("write result code ", String.valueOf(result.code));

            if(result.code == 200) //한줄평 작성이 성공했다면, 한줄평 객체를 인텐트로 보내주기.
            {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("review",etReview.getText().toString());
                resultIntent.putExtra("writer","비회원");
                resultIntent.putExtra("rating",ratingBar.getRating());
                resultIntent.putExtra("time",getDate());
                setResult(RESULT_OK,resultIntent);
                Toast.makeText(getApplicationContext(), "한줄평 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                Log.d("ratingBar.getRating() ", String.valueOf(ratingBar.getRating()));
                finish();
            }else
            {
                Toast.makeText(getApplicationContext(), "한줄평 작성이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public String getDate()
    {
//        Step1. 현재 시간 가져오기.
        long now = System.currentTimeMillis();
//        Step2. Date 생성하기
        Date date = new Date(now);
//        Step3. 가져오고 싶은 형식으로 가져오기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        return  getTime;
    }


}
