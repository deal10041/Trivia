package com.gmail.deal10041.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SubmitActivity extends AppCompatActivity {

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        // get score
        score = getIntent().getIntExtra("score", 0);

        // fill fields
        TextView textView = findViewById(R.id.result);
        textView.setText(getString(R.string.score_end, score));
    }

    public void submitClicked(View v) {

        // get name
        EditText editText = findViewById(R.id.name);

        // create highscore
        Highscore highscore = new Highscore();
        highscore.setName(editText.getText().toString());
        highscore.setScore(score);

        // submit highscore
        HighscoresHelper helper = new HighscoresHelper(this);
        helper.postNewHighscore(highscore);

        // launch highscore activity
        Intent intent = new Intent(SubmitActivity.this, HighscoresActivity.class);
        startActivity(intent);
    }
}
