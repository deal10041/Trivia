package com.gmail.deal10041.trivia;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements TriviaHelper.CallBack{

    private Question question = new Question();
    private int[] buttons = {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4};
    private long score;
    private int questions;
    private int MAX_QUESTIONS = 10;
    private boolean answered;
    private CountDownTimer timer;
    private long secondsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // if no question is asked before
        if(savedInstanceState == null) {
            // get question
            TriviaHelper request = new TriviaHelper(this);
            request.getNextQuestion(this);
            score = 0;
        }
        // if a question is asked before
        else {

            // skip to next question if answered already
            answered = savedInstanceState.getBoolean("answered");
            if(answered) {
                nextQuestion();
            }

            // retrieve question and display it
            question = (Question) savedInstanceState.getSerializable("question");
            score = savedInstanceState.getLong("score");
            questions = savedInstanceState.getInt("questions");
            secondsLeft = savedInstanceState.getLong("time");

            // initiate timer
            final TextView textView = findViewById(R.id.time);
            timer = new CountDownTimer(secondsLeft * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    // update secondsLeft
                    secondsLeft = l / 1000 + 1;

                    // update onscreen timer
                    textView.setText(getString(R.string.time, l / 1000));
                }

                @Override
                public void onFinish() {
                    answered = true;
                    questions += 1;
                    textView.setText(getString(R.string.time, 0));
                    colorButton(null);
                }
            }.start();

            viewQuestion();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // stop timer
        timer.cancel();

        // save question
        outState.putSerializable("question", question);
        outState.putLong("score", score);
        outState.putInt("questions", questions);
        outState.putBoolean("answered", answered);
        outState.putLong("time", secondsLeft);
    }

    @Override
    public void gotQuestion(Question question) {

        this.question = question;
        answered = false;
        secondsLeft = 10;

        // make buttons visible again
        booleanButtons(View.VISIBLE);

        // reset all button colors
        for(int i = 0; i < buttons.length; i++) {
            Button button = findViewById(buttons[i]);
            button.getBackground().clearColorFilter();
        }

        viewQuestion();

        final TextView textView = findViewById(R.id.time);
        timer = new CountDownTimer(secondsLeft * 1000, 1000) {
            @Override
            public void onTick(long l) {
                // update secondsLeft
                secondsLeft = l / 1000 + 1;

                // update onscreen timer
                textView.setText(getString(R.string.time, l / 1000));
            }

            @Override
            public void onFinish() {
                // show right answer
                answered = true;
                questions += 1;
                textView.setText(getString(R.string.time, 0));
                colorButton(null);
            }
        }.start();
    }

    @Override
    public void gotError(String message) {

        // make toast
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast.makeText(context, "Something went wrong", duration).show();
        Log.e("ERROR", message);
    }

    public void buttonClicked(View v) {

        timer.cancel();

        if(!(answered)) {

            answered = true;
            questions += 1;
            Button button = (Button) v;

            // check if answer was correct
            if (questions < MAX_QUESTIONS && button.getText().equals(question.getCorrectAnswer())) {
                score += calculateScore();
            }

            // color buttons and go to next question
            colorButton((Button) v);
        }
    }

    private void viewQuestion() {

        // display question
        TextView textView = findViewById(R.id.question);
        textView.setText(question.getQuestion());

        // display difficulty
        textView = findViewById(R.id.difficulty);
        textView.setText(getString(R.string.difficulty, question.getDifficulty()));

        // set boolean answers
        if(question.getType().equals("boolean")) {
            // make last two buttons invisible
            booleanButtons(View.GONE);
            Button button = findViewById(buttons[0]);
            button.setText("True");
            button = findViewById(buttons[1]);
            button.setText("False");
        }
        // set multiple choice answers
        else {
            ArrayList<String> answers = question.getAnswers();
            for (int i = 0; i < answers.size(); i++) {
                Button button = findViewById(buttons[i]);
                button.setText(answers.get(i));
            }
        }

        // display scores
        textView = findViewById(R.id.score);
        textView.setText(getResources().getString(R.string.score, score));
    }

    private void booleanButtons(int state) {

        // switch visibility state last buttons
        Button button = findViewById(buttons[2]);
        button.setVisibility(state);
        button = findViewById(buttons[3]);
        button.setVisibility(state);
    }

    private void colorButton(Button clickedButton) {

        // color clicked button and right answer
        for(int i = 0; i < question.getAnswers().size(); i++) {
            Button button = findViewById(buttons[i]);
            if(button.getText().equals(question.getCorrectAnswer())) {
                button.getBackground().setColorFilter(0xFF00ff00, PorterDuff.Mode.MULTIPLY);
            }
            else if(clickedButton != null && clickedButton.getId() == buttons[i]) {
                button.getBackground().setColorFilter(0xFFff0000, PorterDuff.Mode.MULTIPLY);
            }
        }

        // go to next question after 2 seconds
        new CountDownTimer(1000,2000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {

                nextQuestion();
            }
        }.start();
    }

    private void nextQuestion() {

        if(questions < MAX_QUESTIONS) {
            // go to next question
            new TriviaHelper(this).getNextQuestion(this);
        }
        else {
            // launch submit highscore
            Intent intent = new Intent(GameActivity.this, SubmitActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
        }
    }

    private long calculateScore() {
        if(question.getDifficulty().equals("hard")) { return 5 * secondsLeft; }
        else if(question.getDifficulty().equals("medium")) { return 3 * secondsLeft; }
        else { return secondsLeft; }
    }
}
