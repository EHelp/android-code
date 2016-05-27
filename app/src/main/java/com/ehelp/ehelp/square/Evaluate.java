package com.ehelp.ehelp.square;

/**
 * Created by Yunzhao on 2016/3/23.
 */
public class Evaluate {
    private String name;
    private float rating;

    public Evaluate(String name, float rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
