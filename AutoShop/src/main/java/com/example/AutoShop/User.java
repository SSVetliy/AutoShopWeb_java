package com.example.AutoShop;

import java.util.ArrayList;

public class User {
    private String name;
    private String sessia;
    private ArrayList<Integer> rule;
    private int id;
    public User(String name, String sessia) {
        this.name = name;
        this.sessia = sessia;
        this.rule = new ArrayList<>();
    }
    public User(){
        this.rule = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSessia() {
        return sessia;
    }
    public void setSessia(String sessia) {
        this.sessia = sessia;
    }
    public ArrayList<Integer> getRule() {
        return rule;
    }
    public boolean checkRule(int rule){
        for(int i : this.rule)
            if(i == rule)
                return true;
        return false;
    }
    public void setRule(int rule) {
        this.rule.add(rule);
    }
    public boolean canAddFeedback(Tovar tovar){
        return DB.canAddFeedback(this, tovar);
    }
}
