package com.example.AutoShop;

public class Tovar {
    private int id;
    private String name;
    private String brand;
    private String description;
    private int price;
    private String img;
    private float rating;
    private boolean display;
    private String data;
    public Tovar(){}
    public boolean isDisplay() {
        return display;
    }
    public void setDisplay(boolean display) {
        this.display = display;
    }
    public int getIntRating() {
        return Math.round(rating);
    }
    public float getRating() {
        return Math.round(rating * 10) / 10f;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
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
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
