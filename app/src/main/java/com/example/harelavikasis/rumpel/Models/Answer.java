package com.example.harelavikasis.rumpel.Models;

import java.io.Serializable;

/**
 * Created by harelavikasis on 14/03/2017.
 */

public class Answer implements Serializable {

    private String answerText;
    private Boolean isRight = false;

    public Answer(){

    }

    public Answer(String answerText, Boolean isRight) {
        this.answerText = answerText;
        this.isRight = isRight;
    }

    public Answer(String answerText) {
        this.answerText =  answerText;
    }

//    public Boolean isRight() {
//        return isRight;
//    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getIsRight() {
        return isRight;
    }

    public void setIsRight(Boolean right) {
        this.isRight = right;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "Context='" + answerText + '\'' +
                '}';
    }
}
