package com.example.a82102.movieprojectfinal.Helper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ServerConnection {
    private static ServerConnection serverConnection = new ServerConnection();

    public static ServerConnection getInstance() {
        return serverConnection;
    }//SeverConnection 객체 리턴

    public void sendRequest(Response.Listener<String> stringListener, Response.ErrorListener errorListener)
    {
        //53866026ec1951a1f4625cf579166162 key
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovieList";
        StringRequest request = new StringRequest(
                Request.Method.GET, //GET방식으로 요청
                url,
                stringListener,
                errorListener
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
    }//end sendRequest.
}
