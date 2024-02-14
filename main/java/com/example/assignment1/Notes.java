package com.example.assignment1;

public class Notes {

    private long id;
    private  String title;
    private String notes;

    private String date;

    private String time;
    public Notes(long id, String title, String notes,String date,String time){
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.date = date;
        this.time = time;
    }



    public long getId() {

        return id;
    }

    public String getTitle() {

        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNotes() {

        return notes;
    }

}