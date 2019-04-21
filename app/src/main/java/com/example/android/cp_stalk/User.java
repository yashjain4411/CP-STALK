package com.example.android.cp_stalk;


public class User {
    public String name, email, handle;

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public User(String name, String email, String handle) {
        this.name = name;
        this.email = email;
        this.handle = handle;
    }
}
