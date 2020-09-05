package com.example.tjg.cahouseadmin.JavaFiles;

public class News {

    String news_id;
    String news_title;
    String news_description;
    String news_time;
    String news_date;
    String news_status;

    public News(String news_id, String news_title, String news_description, String news_date, String news_time, String news_status) {
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_description = news_description;
        this.news_date = news_date;
        this.news_time = news_time;
        this.news_status = news_status;
    }

    public String getNews_title() {
        return news_title;
    }

    public String getNews_description() {
        return news_description;
    }

    public String getNews_id() {
        return news_id;
    }

    public String getNews_time() {
        return news_time;
    }

    public String getNews_date() {
        return news_date;
    }


    public String getNews_status() {
        return news_status;
    }


}
