package com.example.AutoShop;

public class Feedback {
    private int id;
    private int id_tovar;
    private String name;
    private int rating;
    private String commentary;
    public Feedback(int id, int id_tovar, String name, int rating, String commentary) {
        this.id = id;
        this.id_tovar = id_tovar;
        this.name = name;
        this.rating = rating;
        this.commentary = commentary;
    }
    public Feedback(){}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId_tovar() {
        return id_tovar;
    }
    public void setId_tovar(int id_tovar) {
        this.id_tovar = id_tovar;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getCommentary() {
        return commentary;
    }
    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}
