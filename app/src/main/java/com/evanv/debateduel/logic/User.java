package com.evanv.debateduel.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    /** Public instance fields **/
    public String NAME;
    public String USERNAME;
    public String STATE;
    public List<String> PICKS;
    public List<String> LAST_WEEK_PICKS;
    public int SCORE;
    public String IMAGE;
    public String EMAIL;
    public List<String> INVITE_TO_LEAGUE;
    public List<String> INVITE_FROM_USER;

    public User(String name, String email, String state, String username) {
        NAME = name;
        EMAIL = email;
        STATE = state;
        USERNAME = username;
        PICKS = new ArrayList<>();
        LAST_WEEK_PICKS = new ArrayList<>();
        SCORE = 0;
        INVITE_TO_LEAGUE = new ArrayList<>();
        INVITE_FROM_USER = new ArrayList<>();
        IMAGE = "https://debateduelbucket.s3.us-east-2.amazonaws.com/user_default.png";
    }

    public User(String name, String email, String state, String username, String picks,
                String last_week_picks, String score, String image, String invitesTo, String invitesFrom) {
        NAME = name;
        EMAIL = email;
        STATE = state;
        USERNAME = username;
        SCORE = Integer.parseInt(score);
        IMAGE = image;
        if(picks != null) {
            PICKS = Arrays.asList(picks.split(",", -1));
        }
        if(last_week_picks != null) {
            LAST_WEEK_PICKS = Arrays.asList(last_week_picks.split(",", -1));
        }
        if(invitesTo != null) {
            INVITE_TO_LEAGUE = Arrays.asList(invitesTo.split(",", -1));
        }
        if(invitesFrom != null) {
            INVITE_FROM_USER = Arrays.asList(invitesFrom.split(",", -1));
        }
    }
}
