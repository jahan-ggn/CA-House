package com.example.tjg.cahouseadmin.JavaFiles;


public class Documents {

    String id;
    String document_name;

    public Documents(String id, String document_name) {
        this.id = id;
        this.document_name = document_name;
    }

    public String getId() {
        return id;
    }

    public String getDocument_name() {
        return document_name;
    }
}
