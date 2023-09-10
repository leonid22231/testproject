package com.test123.myapplication.fragments.game.models;

import java.util.List;

public class Level {
    int id;
    List<Question> questions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
