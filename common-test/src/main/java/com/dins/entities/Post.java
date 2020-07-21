package com.dins.entities;

import com.dins.ToJSON;

public class Post extends ToJSON {
    private long id;
    private long userId;
    private String title;
    private String body;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
