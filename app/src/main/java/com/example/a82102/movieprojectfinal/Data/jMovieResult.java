package com.example.a82102.movieprojectfinal.Data;

import java.util.ArrayList;

// Json에서 객체(Object)의 시작은 {} 중괄호를 사용하고 배열은 [] 대괄호를 사용합니다.
public class jMovieResult {
   public String message;
    public int code;
    public String resultType;
    public ArrayList<jMovie> result = new ArrayList();

}
