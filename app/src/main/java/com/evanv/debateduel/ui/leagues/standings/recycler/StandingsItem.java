package com.evanv.debateduel.ui.leagues.standings.recycler;

public class StandingsItem {
    private String mUserScore; // User's position in league
    private String mUserName;  // Name of League
    private String mUserImage; // Icon for league

    public StandingsItem(String name, String position, String image) {
        mUserName = name;
        mUserScore = position;
        mUserImage = image;
    }

    public String getUserScore() {
        return mUserScore;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserImage() {
        return mUserImage;
    }
}
