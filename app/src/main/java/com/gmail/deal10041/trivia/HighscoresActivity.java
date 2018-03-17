package com.gmail.deal10041.trivia;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.ArrayList;

public class HighscoresActivity extends AppCompatActivity implements HighscoresHelper.CallBack{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        HighscoresHelper helper = new HighscoresHelper(this);
        helper.getHighscores(this);
    }

    @Override
    public void gotHighscores(ArrayList<Highscore> highscores) {

        ListView listView = findViewById(R.id.highscores);
        listView.setAdapter(new HighscoresAdapter(this, R.layout.highscore, highscores));
    }

    @Override
    public void gotError(String message) {
        // make toast
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        String text = "Something went wrong...";
        Toast.makeText(context, text, duration).show();

        // log error
        Log.e("Error", message);
    }
}
