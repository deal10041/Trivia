package com.gmail.deal10041.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BeginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
    }

    public void toQuestions(View v) {

        Intent intent = new Intent(BeginActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
