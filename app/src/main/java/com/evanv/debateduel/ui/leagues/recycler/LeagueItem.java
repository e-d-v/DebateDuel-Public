package com.evanv.debateduel.ui.leagues.recycler;

public class LeagueItem {
    private String mUserPosition;    // User's position in league
    private String mLeagueName;      // Name of League
    private String mLeagueImg;       // Icon for league

    public LeagueItem(String name, String position, String image) {
        mLeagueName = name;
        mUserPosition = position;
        mLeagueImg = image;
    }

    public String getUserPosition() {
        return mUserPosition;
    }

    public String getLeagueName() {
        return mLeagueName;
    }

    public String getLeagueImage() {
        return mLeagueImg;
    }
}
