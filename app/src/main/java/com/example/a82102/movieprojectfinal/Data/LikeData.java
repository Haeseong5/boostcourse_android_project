package com.example.a82102.movieprojectfinal.Data;

public class LikeData {
    String likeyn="";
    int likeStatus;
    String dislikeyn="";
    int dislikeStatus;

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public int getDislikeStatus() {
        return dislikeStatus;
    }

    public void setDislikeStatus(int dislikeStatus) {
        this.dislikeStatus = dislikeStatus;
    }

    public String getDislikeyn() {
        return dislikeyn;
    }

    public void setDislikeyn(String dislikeyn) {
        this.dislikeyn = dislikeyn;
    }

    public String getLikeyn() {
        return likeyn;
    }

    public void setLikeyn(String likeyn) {
        this.likeyn = likeyn;
    }
}
