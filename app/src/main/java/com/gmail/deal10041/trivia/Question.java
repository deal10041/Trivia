package com.gmail.deal10041.trivia;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dylan Wellner on 14-3-2018.
 * Model class for a question
 */

public class Question implements Serializable{

    private String question;
    private ArrayList<String> answers;
    private String correctAnswer;
    private String type;
    private String difficulty;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() { return difficulty; }

    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
