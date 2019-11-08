package com.example.a82102.movieprojectfinal.Item;

import android.graphics.drawable.Drawable;

public class MovieItem {
    String title;
    String age;
    String rating;
    String date;
    String genre;
    String posterURL;

    public MovieItem(String title, String age, String rating, String date, String genre, String posterURL)
    {
        this.posterURL = posterURL;
        this.title = title;
        this.age = age;
        this.rating = rating;
        this.date = date;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getAge() {
        return age;
    }

    public String getRating() {
        return rating;
    }


    public String getDate() {
        return date;
    }


    public String getGenre() {
        return genre;
    }

    public String getPosterURL() {
        return posterURL;
    }
}
