package com.gmail.deal10041.trivia;

import android.support.annotation.NonNull;

/**
 * Created by Gebruiker on 16-3-2018.
 * Model class for a highscore
 */

public class Highscore implements Comparable{

    private String name;
    private long score;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public long getScore() { return score; }

    public void setScore(long score) { this.score = score; }

    @Override
    public int compareTo(@NonNull Object compareHigh) {
        long compareScore = ((Highscore)compareHigh).getScore();

        if(compareScore > score) {
            return 1;
        }
        else if(compareScore < score) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
