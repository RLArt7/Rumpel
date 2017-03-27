package com.example.harelavikasis.rumpel.Models;

/**
 * Created by harelavikasis on 27/03/2017.
 */

public class Contact {

    private String name ;
    private String id;

    public Contact(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
