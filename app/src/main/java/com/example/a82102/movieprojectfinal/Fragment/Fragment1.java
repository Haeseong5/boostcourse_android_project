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
import android.widget.Toast;

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
import com.example.a82102.movieprojectfinal.Helper.Database;
import com.example.a82102.movieprojectfinal.Helper.NetworkHelper;
import com.example.a82102.movieprojectfinal.Item.MovieItem;
import com.example.a82102.movieprojectfinal.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.a82102.movieprojectfinal.Helper.NetworkHelper.TYPE_MOBILE;
import static com.example.a82102.movieprojectfinal.Helper.NetworkHelper.TYPE_NOT_CONNECTED;
import static com.example.a82102.movieprojectfinal.Helper.NetworkHelper.TYPE_WIFI;

public class Fragment1 extends android.support.v4.app.Fragment {
    ArrayList<MovieItem> movieList = new ArrayList<>();
    ImageView ivMoviePoster;
    TextView tvMovieTitle;
    TextView tvMovieNumber;
    TextView tvReservation;
    TextView tvGrade;
    TextView tvDday;
    TextView tvGenre;
    Button btDetail;
    int position; //영화 인덱스
    int type=1; //정렬 타입
    int networkStatus;

    public static  Fragment1 newInstance(int position){
        Fragment1 fragment1 = new Fragment1();
        // Supply position input as an argument.
        Log.d("Fragment Address: ", String.valueOf(fragment1));
        Log.d("Fragment position: ", String.valueOf(position));

        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment1.setArguments(args);
        return fragment1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position", 0);
        Log.d("bundle position: ", String.valueOf(position));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        ivMoviePoster = rootView.findViewById(R.id.fragment_movie_poster);
        tvMovieTitle = rootView.findViewById(R.id.fragment_movie_title);
        btDetail = rootView.findViewById(R.id.fragment_movie_detail_button);
        tvGrade = rootView.findViewById(R.id.fragment_movie_age);
        tvReservation = rootView.findViewById(R.id.fragment_movie_reservation);
        tvMovieNumber = rootView.findViewById(R.id.fragment_movie_number);
        tvDday = rootView.findViewById(R.id.fragment_movie_dday);
        networkStatus = NetworkHelper.getConnectivityStatus(getActivity());
        Database.openDatabase(getActivity(),"movie_db");
        Database.createTable("outline");

        btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        if(AppHelper.requestQueue == null)
        {
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity());
        }
        movieListRequest(type);

        return rootView;
    }


    protected void movieListRequest(final int type)
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
//                영화순위 구분 1 : 예매율순 2 : 큐레이션 3 : 상영예정작
                Map<String,String> params = new HashMap<String,String>();
                params.put("type", String.valueOf(type));
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
            jMovie movie = result.get(position);
            checkNetworkStatus(movie.reservation_grade,movie.title,movie.reservation_rate,movie.grade,movie.image);
            String grade = Integer.toString(movie.grade)+"세 관람가";
            String reservation =" "+Float.toString(movie.reservation_rate)+"%";
            Glide.with(getActivity()).load(movie.image).into(ivMoviePoster);
            tvMovieTitle.setText(movie.title);
            tvGrade.setText(grade);
            tvReservation.setText(reservation);
            tvMovieNumber.setText(Integer.toString(movie.reservation_grade)+". ");
        }else{
            checkNetworkStatus(0,null,0,0,null);
        }
    }

    protected void checkNetworkStatus(int id, String title, float reservation, int grade, String image){
        switch (networkStatus){
            case TYPE_WIFI:
                Database.insertData(id, title, reservation, grade, image);
//                Database.selectData("movie");
//                Database.selectData("movie");
                break;
            case TYPE_MOBILE:
                Database.insertData(id, title, reservation, grade, image);
                break;
            case TYPE_NOT_CONNECTED:
                Toast.makeText(getActivity(),"인터넷 연결 안 됨. CODE:"+Integer.toString(TYPE_NOT_CONNECTED),Toast.LENGTH_SHORT).show();
                Database.selectData("movie");
//                Database.insertData(id, title, reservation, grade, image);
                break;
            default:
                break;
        }
    }
}
