package com.eastflag.secondproject.domain;

/**
 * Created by Administrator on 2016-04-23.
 */
public class BookVO {
    private String title;
    private String author;
    private String imgUrl;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
