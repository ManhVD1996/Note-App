package com.example.manhvd.noteapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private int id;
    private String title;
    private String body;
    private String time;
    private String timeFull;
    private String pathImage;
//    private String color;

    public Note() {
    }

    public Note(int id, String title, String body, String time, String timeFull) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.time = time;
        this.timeFull = timeFull;
    }

    public Note(String title, String body, String time, String timeFull) {
        this.title = title;
        this.body = body;
        this.time = time;
        this.timeFull = timeFull;
    }

    public Note(String title, String body, String time) {
        this.title = title;
        this.body = body;
        this.time = time;
    }

    public Note(int id, String pathImage) {
        this.id = id;
        this.pathImage = pathImage;
    }

    public Note(String pathImage) {
        this.pathImage = pathImage;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        body = in.readString();
        time = in.readString();
        timeFull = in.readString();
        pathImage = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getTimeFull() {
        return timeFull;
    }

    public void setTimeFull(String timeFull) {
        this.timeFull = timeFull;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(time);
        dest.writeString(timeFull);
        dest.writeString(pathImage);
    }

    //    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
}
