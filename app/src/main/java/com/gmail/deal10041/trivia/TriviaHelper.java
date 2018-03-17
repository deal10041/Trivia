package com.gmail.deal10041.trivia;

import android.content.Context;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dylan Wellner on 14-3-2018.
 * Implements a helper class for providing questions.
 */

public class TriviaHelper implements Response.Listener<JSONObject>, Response.ErrorListener{

    String URL = "https://opentdb.com/api.php?amount=1&encode=urlLegacy";

    public interface CallBack {
        void gotQuestion(Question question);
        void gotError(String message);
    }

    private Context context;
    private CallBack delegate;

    public TriviaHelper(Context context) { this.context = context; }

    public void getNextQuestion(CallBack activity) {

        // make new request for data
        RequestQueue queue = Volley.newRequestQueue(context);
        delegate = activity;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, null, this, this);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        delegate.gotError(error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {

        Question question = new Question();

        try {
            // get array of questions
            JSONArray array = response.getJSONArray("results");

            // fill question
            JSONObject jsonObject = array.getJSONObject(0);
            question.setQuestion(convertString(jsonObject.getString("question")));
            question.setCorrectAnswer(convertString(jsonObject.getString("correct_answer")));
            question.setType(jsonObject.getString("type"));

            // get all answers
            ArrayList<String> answers = new ArrayList<>();
            JSONArray items = jsonObject.getJSONArray("incorrect_answers");
            answers.add(convertString(jsonObject.getString("correct_answer")));
            for(int i = 0; i < items.length(); i++) { answers.add(convertString(items.getString(i))); }

            // shuffle answers
            Collections.shuffle(answers);
            question.setAnswers(answers);


        } catch (JSONException e) { System.err.println(e); }

        delegate.gotQuestion(question);
    }

    private String convertString(String text) {

        String decodedText = "";
        try {
            decodedText = URLDecoder.decode(text, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Log.e("Error", e.getMessage());
        }
        return decodedText;
    }
}
