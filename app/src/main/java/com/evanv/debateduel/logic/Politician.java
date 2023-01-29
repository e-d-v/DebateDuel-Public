package com.evanv.debateduel.logic;

import java.util.ArrayList;

public class Politician {
    public String NAME;
    public String PARTY;
    public String BELIEFS;
    public int ID;
    public int AGE;
    public int CURRENT_SCORE;
    public ArrayList<Score> SCORES;
    public ArrayList<Integer> PREVIOUS_SCORES;
    public String IMAGE;

    public Politician(String name, String party, int age, String beliefs, String image, int id) {
        this.NAME = name;
        this.PARTY = party;
        this.AGE = age;
        this.BELIEFS = beliefs;
        this.IMAGE = image;
        this.CURRENT_SCORE = 0;
        this.PREVIOUS_SCORES = new ArrayList<>();
        this.ID = id;
        this.SCORES = new ArrayList<>();
    }
}
