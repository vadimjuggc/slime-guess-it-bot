package com.mygame;

import java.util.Objects;

public class Track {
    private String title;
    private String artist;
    private String audioPath;


    public Track(String title, String artist, String audioPath) {
        this.title = title;
        this.artist = artist;
        this.audioPath = audioPath;
    }

    public Track(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAudioPath() {
        return audioPath;
    }
}
