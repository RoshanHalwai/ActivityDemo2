package com.example.hp.activitydemo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Item {
    private String id;
    private String item;
    private String price;
    private String description;
    private String url;
    private String quantity;

    public Item() {
        //this constructor is required
    }

    public Item(String id, String item, String price, String description, String url, String quantity) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.description = description;
        this.url = url;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
