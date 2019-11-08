package com.example.a82102.movieprojectfinal.Item;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewItem implements Parcelable{
    String profile;
    String id;
    String time;
    String review;
    String recommand;
    float rating;

    public ReviewItem(String profile, String id, String time, String review, String recommand, float rating) {
        this.profile = profile;
        this.id = id;
        this.time = time;
        this.review = review;
        this.recommand = recommand;
        this.rating = rating;
    }

    public String getProfile() {
        return profile;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getReview() {
        return review;
    }

    public String getRecommand() {
        return recommand;
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.profile);
        dest.writeString(this.id);
        dest.writeString(this.review);
        dest.writeString(this.time);
        dest.writeString(this.recommand);

    }
    public ReviewItem(Parcel in)
    {
        this.profile = in.readString();
        this.id = in.readString();
        this.review = in.readString();
        this.time = in.readString();
        this.recommand = in.readString();
    }
    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public ReviewItem createFromParcel(Parcel in) {
            return new ReviewItem(in);
        }

        @Override
        public ReviewItem[] newArray(int size) {
            // TODO Auto-generated method stub
            return new ReviewItem[size];
        }

    };


}
