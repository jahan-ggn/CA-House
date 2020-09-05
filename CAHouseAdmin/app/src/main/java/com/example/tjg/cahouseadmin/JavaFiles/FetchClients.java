package com.example.tjg.cahouseadmin.JavaFiles;

public class FetchClients {
    String id;
    String name;
    String email;
    String mobile;

    public FetchClients(String id, String name, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}
