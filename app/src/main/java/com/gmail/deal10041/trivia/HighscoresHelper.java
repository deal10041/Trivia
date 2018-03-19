package com.gmail.deal10041.trivia;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Gebruiker on 16-3-2018.
 * Helper class for firebase
 */

public class HighscoresHelper {

    public interface CallBack {
        void gotHighscores(ArrayList<Highscore> highscores);
        void gotError(String message);
    }

    private Context context;
    private CallBack delegate;
    private DatabaseReference database;

    public HighscoresHelper(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance().getReference("highscores");
    }

    public void postNewHighscore(Highscore highscore) {

        // add highscore to database
        DatabaseReference ref = database.push();
        ref.setValue(highscore);
    }

    public void getHighscores(CallBack activity) {

        delegate = activity;

        // get highscores
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Highscore> highscores = new ArrayList<>();

                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    highscores.add(child.getValue(Highscore.class));
                }

                // sort scores
                Collections.sort(highscores);
                delegate.gotHighscores(highscores);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                delegate.gotError(databaseError.getMessage());
            }
        });
    }
}
