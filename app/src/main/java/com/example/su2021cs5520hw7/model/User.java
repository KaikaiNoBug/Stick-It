package com.example.su2021cs5520hw7.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;


@IgnoreExtraProperties
public class User implements Serializable {
    public String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return username;
    }

    public boolean EqualsTo(User other)
    {
        return username.equals(other.getUsername());
    }
}