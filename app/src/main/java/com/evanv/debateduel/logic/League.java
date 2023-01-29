package com.evanv.debateduel.logic;

import java.util.ArrayList;
import java.util.List;

public class League {
    public String NAME;
    public List<User> USERS;
    public String PROFILE_PICTURE;
    public int ID;

    public League(int ID, String NAME) {
        this.NAME = NAME;
        this.USERS = new ArrayList<>();
        this.PROFILE_PICTURE = "https://debateduelbucket.s3.us-east-2.amazonaws.com/league_defualt.png";
        this.ID = ID;
    }

    public League(String ID, String NAME, List<User> USERS, String PROFILE_PICTURE) {
        this.NAME = NAME;
        this.USERS = USERS;
        this.PROFILE_PICTURE = PROFILE_PICTURE;
        this.ID = Integer.parseInt(ID);
    }

    public void updatePicture(String pic) {
        this.PROFILE_PICTURE = pic;
    }
}
