package com.example.harelavikasis.rumpel.Models;

import com.example.harelavikasis.rumpel.Managers.ChatManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harelavikasis on 14/03/2017.
 */

public class Chat implements Serializable {
    private String Id;
    private String endPoint;
    private List<Question> questions = new ArrayList<Question>();

    public Chat(){}

    public Chat(String endPointId ,String id) {
        this.Id = id;
        this.endPoint = endPointId;
    }
    public Chat(String endPoint) {
        this.endPoint = endPoint;

    }

    public Chat(Chat c) {
        this.Id = c.getId();
        this.endPoint = c.getEndPoint();
        this.questions = c.getQuestions();
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {

        return "{" +
                "\"endPointId\":" + endPoint  +
                ",\"questions\":" + questions +
//                ",\"cManger\":" + cManger +
                '}';
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String toJson(){
        Gson gson = new Gson();
        JsonObject jsonobject = new JsonParser().parse(this.toString()).getAsJsonObject();
        JsonArray jsonarray = jsonobject.getAsJsonArray("questions");
        String json = jsonobject.toString();


        return json;
    }

    public Boolean isThereOpenQuestion()
    {
        if (questions != null) {
            for (Question quest : questions) {
                if (quest.getQuestionOpen()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Question getOpenQuestion()
    {
        if (questions != null) {
            for (Question quest : questions) {
                if (quest.getQuestionOpen()) {
                    return questions.get(questions.indexOf(quest));
                }
            }
        }
        return null;
    }
}
