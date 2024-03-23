package com.byteclass.noteslide.model;

public class Note {

    private int id;
    private String topic;
    private String note;

    public Note(String topic, String note)
    {
        this.topic = topic;
        this.note = note;
    }

    public int getId()
    {
        return id;
    }

    public String getTopic()
    {
        return topic;
    }

    public String getNote()
    {
        return note;
    }
}
