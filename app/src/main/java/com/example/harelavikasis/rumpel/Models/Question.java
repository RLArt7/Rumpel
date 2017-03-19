package com.example.harelavikasis.rumpel.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by harelavikasis on 14/03/2017.
 */

public class Question implements Serializable {
    private String id;
    private String questionText;
    private String senderId;
    private List<Answer> answers = new ArrayList<>();
    private Date initialTime;
    int timeToAnswer;

    private Boolean isRightAnswer = false;
    private Boolean isQuestionOpen = true;
    public Question(){}

    public Question(String id, String questText, List<Answer> answers) {
        this.id = id;
        this.questionText = questText;
        this.answers = answers;
        initialTime = new Date();
    }

    public Question(String id, String questText, int timeToAnswer) {
        this.id = id;
        this.questionText = questText;
        this.timeToAnswer = timeToAnswer;
    }

    public void setIsRightAnswer(Boolean rightAnswer) {
        isRightAnswer = rightAnswer;
    }

    public Boolean getIsRightAnswer() {
        return isRightAnswer;
    }

    public int getTimeToAnswer() {
        return timeToAnswer;
    }

    public void setTimeToAnswer(int timeToAnswer) {
        this.timeToAnswer = timeToAnswer;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setContext(String questText) {
        this.questionText = questText;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void closeQuestion() {
        this.isQuestionOpen = false;
    }

    public boolean checkAnswer(Answer answer) {
        closeQuestion();
        if (answer.getIsRight()) {
            isRightAnswer = true;
            return true;
        }
        return false;
    }

//    public Boolean isQuestionOpen() {
//        return isQuestionOpen;
//    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Boolean getQuestionOpen() {
        return isQuestionOpen;
    }

    public void setQuestionOpen(Boolean questionOpen) {
        isQuestionOpen = questionOpen;
    }

    @Override
    public String toString() {

        return "Question{" +
                "context='" + questionText + '\'' +
                "Your Answer was : " + (isRightAnswer ? "right" : "wrong") +
                " you answer in: " + timeToAnswer + "sec" +
                '}';
    }
}
