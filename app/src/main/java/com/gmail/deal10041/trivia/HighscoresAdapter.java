package com.gmail.deal10041.trivia;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gebruiker on 16-3-2018.
 * Implements adapter for highscores
 */

public class HighscoresAdapter extends ArrayAdapter<Highscore> {

    private ArrayList<Highscore> highscores;

    public HighscoresAdapter(@NonNull Context context, int resource, ArrayList<Highscore> highscores) {
        super(context, resource, highscores);
        this.highscores = highscores;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.highscore, parent, false);
        }

        // fill in fields
        TextView textView = convertView.findViewById(R.id.name);
        textView.setText(getContext().getResources().getString(R.string.name, highscores.get(position).getName()));

        textView = convertView.findViewById(R.id.score);
        textView.setText(getContext().getResources().getString(R.string.score, highscores.get(position).getScore()));

        return convertView;
    }
}
