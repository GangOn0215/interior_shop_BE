package amo.java;

import lombok.Getter;

@Getter
public class User {
    private String name;
    private String pass;

    public User setName(String name) {
        this.name = name;

        return this;
    }

    public User setPass(String pass) {
        this.pass = pass;

        return this;
    }
}
