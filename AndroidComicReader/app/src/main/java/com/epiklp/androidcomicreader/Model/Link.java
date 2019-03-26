package com.epiklp.androidcomicreader.Model;

public class Link {
    public int ID;
    public String Link;
    public int ChapterID;

    public Link(int ID, String link, int chapterID) {
        this.ID = ID;
        Link = link;
        ChapterID = chapterID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getChapterID() {
        return ChapterID;
    }

    public void setChapterID(int chapterID) {
        ChapterID = chapterID;
    }
}
