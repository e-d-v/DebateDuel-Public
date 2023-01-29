package com.evanv.debateduel.logic;

import java.util.ArrayList;

public class Score {
    public String description;
    public int value;


    public Score(String description, int score) {
        this.description = description;
        this.value = score;
    }

    @Override
    public String toString() {
        if (value > 0) {
            return "+" + value + " " + description + ", ";
        } else {
            return "-" + value + " " + description + ", ";
        }
    }
}
