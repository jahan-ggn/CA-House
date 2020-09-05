package com.example.tjg.cahouseadmin.JavaFiles;

/**
 * Created by apple on 3/19/18.
 */

public class Feedback {
    String feedid;
    String feedname;
    String feedemail;
    String feedmsg;
    String feeddate;
    String feedtime;

    public Feedback(String feedid, String feedname, String feedemail, String feedmsg, String feeddate, String feedtime) {
        this.feedid = feedid;
        this.feedname = feedname;
        this.feedemail = feedemail;
        this.feedmsg = feedmsg;
        this.feeddate = feeddate;
        this.feedtime = feedtime;
    }

    public String getFeedid() {
        return feedid;
    }

    public String getFeedname() {
        return feedname;
    }

    public String getFeedemail() {
        return feedemail;
    }

    public String getFeedmsg() {
        return feedmsg;
    }

    public String getFeeddate() {
        return feeddate;
    }

    public String getFeedtime() {
        return feedtime;
    }
}