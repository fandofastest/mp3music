package com.prober.freemusicapp.model;


public class TrackOnline {
    private String songTitle, songImg, songUrl, songUsr,songId;
    private int songLikes, songDur;


    public TrackOnline(String songTitle, String songImg, String songUrl, String songUsr, int songLikes, int songDur,String songId) {
        this.songTitle = songTitle;
        this.songImg = songImg;
        this.songUrl = songUrl;
        this.songUsr = songUsr;
        this.songLikes = songLikes;
        this.songDur = songDur;
        this.songId = songId;

    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongImg() {
        return songImg;
    }

    public void setSongImg(String songImg) {
        this.songImg = songImg;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSongUsr() {
        return songUsr;
    }

    public void setSongUsr(String songUsr) {
        this.songUsr = songUsr;
    }

    public int getSongLikes() {
        return songLikes;
    }

    public void setSongLikes(int songLikes) {
        this.songLikes = songLikes;
    }

    public int getSongDur() {
        return songDur;
    }

    public void setSongDur(int songDur) {
        this.songDur = songDur;
    }
}
