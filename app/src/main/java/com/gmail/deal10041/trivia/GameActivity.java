package com.gmail.deal10041.trivia;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements TriviaHelper.CallBack{

    private Question question = new Question();
    private int[] buttons = {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4};
    private int score;
    private int questions;
    private int MAX_QUESTIONS = 10;

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
            // retrieve question and display it
            question = (Question) savedInstanceState.getSerializable("question");
            score = savedInstanceState.getInt("score");
            questions = savedInstanceState.getInt("questions");

            viewQuestion();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save question
        outState.putSerializable("question", question);
        outState.putInt("score", score);
        outState.putInt("questions", questions);
    }

    @Override
    public void gotQuestion(Question question) {

        this.question = question;

        if(questions > 0) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // make buttons visible again
        booleanButtons(View.VISIBLE);

        // reset all button colors
        for(int i = 0; i < buttons.length; i++) {
            Button button = findViewById(buttons[i]);
            button.getBackground().clearColorFilter();
        }

        viewQuestion();
    }

    @Override
    public void gotError(String message) {

        // make toast
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast.makeText(context, message, duration).show();
    }

    public void buttonClicked(View v) {

        questions += 1;

        if(questions < MAX_QUESTIONS) {
            // check if answer was correct
            Button button = (Button) v;
            if(button.getText().equals(question.getCorrectAnswer())) {
                score += 1;
            }

            // show correct answer
            colorButton(button);

            // go to next question
            new TriviaHelper(this).getNextQuestion(this);
;
        }
        else {

            // launch submit highscore
            Intent intent = new Intent(GameActivity.this, SubmitActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
        }
    }

    public void viewQuestion() {

        // display question
        TextView textView = findViewById(R.id.question);
        textView.setText(question.getQuestion());

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
        Button button = findViewById(buttons[2]);
        button.setVisibility(state);
        button = findViewById(buttons[3]);
        button.setVisibility(state);
    }

    private void colorButton(Button clickedButton) {
        for(int i = 0; i < question.getAnswers().size(); i++) {
            Button button = findViewById(buttons[i]);
            if(button.getText().equals(question.getCorrectAnswer())) {
                button.getBackground().setColorFilter(0xFF00ff00, PorterDuff.Mode.MULTIPLY);
            }
            else if(clickedButton.getId() == buttons[i]) {
                button.getBackground().setColorFilter(0xFFff0000, PorterDuff.Mode.MULTIPLY);
            }
        }
    }
}
