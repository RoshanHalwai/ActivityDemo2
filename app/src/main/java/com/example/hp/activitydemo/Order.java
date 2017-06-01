package com.example.hp.activitydemo;

/**
 * Created by user on 5/31/2017.
 */

public class Order {
    public String Item_Name;
    public String Item_Quantity;

    public Order() {
    }

    public Order(String itemName, String itemQuantity) {
        this.Item_Name = itemName;
        this.Item_Quantity = itemQuantity;
    }
}


