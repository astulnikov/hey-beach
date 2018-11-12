package com.dvipersquad.heybeach.auth;

public class User {

    String id;

    String email;

    public User(String id, String email, String token) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}