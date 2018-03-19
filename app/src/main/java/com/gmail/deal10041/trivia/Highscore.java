package com.gmail.deal10041.trivia;

import android.support.annotation.NonNull;

/**
 * Created by Gebruiker on 16-3-2018.
 * Model class for a highscore
 */

public class Highscore implements Comparable{

    private String name;
    private int score;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    @Override
    public int compareTo(@NonNull Object compareHigh) {
        int compareScore = ((Highscore)compareHigh).getScore();

        return compareScore - score;
    }
}
