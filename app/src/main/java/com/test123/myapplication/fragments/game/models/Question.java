package com.test123.myapplication.fragments.game.models;

import android.media.Image;

import java.util.List;

public class Question {
    int id;
    String Question_text;
    String Question_image;

    List<Answer> Question_answers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion_text() {
        return Question_text;
    }

    public void setQuestion_text(String question_text) {
        Question_text = question_text;
    }

    public String getQuestion_image() {
        return Question_image;
    }

    public void setQuestion_image(String question_image) {
        Question_image = question_image;
    }

    public List<Answer> getQuestion_answers() {
        return Question_answers;
    }

    public void setQuestion_answers(List<Answer> question_answers) {
        Question_answers = question_answers;
    }
}
