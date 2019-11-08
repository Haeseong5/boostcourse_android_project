package com.example.a82102.movieprojectfinal.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.a82102.movieprojectfinal.Activity.DetailActivity;
import com.example.a82102.movieprojectfinal.Data.jMovie;
import com.example.a82102.movieprojectfinal.Data.jMovieResult;
import com.example.a82102.movieprojectfinal.Helper.AppHelper;
import com.example.a82102.movieprojectfinal.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment2 extends android.support.v4.app.Fragment {
    ImageView ivMoviePoster;
    TextView tvMovieTitle;
    TextView tvMovieNumber;
    TextView tvReservation;
    TextView tvGrade;
    TextView tvGenre;
    TextView tvDday;
    Button btDetail;
    int position = 2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        ivMoviePoster = rootView.findViewById(R.id.fragment_movie_poster);
        tvMovieTitle = rootView.findViewById(R.id.fragment_movie_title);
        btDetail = rootView.findViewById(R.id.fragment_movie_detail_button);
        tvGrade = rootView.findViewById(R.id.fragment_movie_age);
        tvMovieNumber = rootView.findViewById(R.id.fragment_movie_number);
        tvDday = rootView.findViewById(R.id.fragment_movie_dday);
        tvReservation = rootView.findViewById(R.id.fragment_movie_reservation);
        btDetail = rootView.findViewById(R.id.fragment_movie_detail_button);
        btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        if(AppHelper.requestQueue == null)
        {
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity());
        }
        sendRequest();
        return rootView;
    }

    protected void sendRequest()
    {
        //53866026ec1951a1f4625cf579166162 key
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovieList";
        StringRequest request = new StringRequest(
                Request.Method.POST, //GET방식으로 요청
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
                //서버에서 요청한 데이터를 넘겨줄 때 사용
                Map<String,String> params = new HashMap<String,String>();
                params.put("id","id");
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }//end movieListRequest.
    protected void processResponse(String response) //응답결과를 처리하는 메소드
    {
        Gson gson = new Gson();
        jMovieResult movieResult = gson.fromJson(response, jMovieResult.class);
        if(movieResult != null)
        {
            ArrayList<jMovie> result = movieResult.result; //movieResult클래스에 잇는 리스트를 대입시킨다.
            Log.d("message: ", movieResult.message);
            jMovie movie = result.get(1);

            String grade = Integer.toString(movie.grade)+"세 관람가";
            String reservation =" "+Float.toString(movie.reservation_rate)+"%";

            Glide.with(getActivity()).load(movie.image).into(ivMoviePoster);
            tvMovieNumber.setText(Integer.toString(movie.reservation_grade)+". ");
            tvMovieTitle.setText(movie.title);
            tvGrade.setText(grade);
            tvReservation.setText(reservation);
            tvMovieNumber.setText("2. ");
        }
    }
}
