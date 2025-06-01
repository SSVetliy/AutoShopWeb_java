package com.example.AutoShop;

public class Categiry {
    private int id;
    private String name;
    private String img;

    private String href;

    public Categiry(int id, String name, String img, String href) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.href = href;
    }

    public Categiry(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
