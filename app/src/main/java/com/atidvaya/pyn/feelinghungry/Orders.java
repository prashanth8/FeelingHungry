package com.atidvaya.pyn.feelinghungry;

/**
 * Created by pyn on 16-02-2016.
 */
public class Orders
{

    String name;
    String mobile_number;
    String food_item_name;
    String quantity;
    String delivery_point;
    String date_time;
    String total_price;
    String food_item_price_per_unit;


    public Orders(String name, String mobile_number, String food_item_name, String quantity, String date_time, String delivery_point, String total_price, String food_item_price_per_unit) {
        this.name = name;
        this.mobile_number = mobile_number;
        this.food_item_name = food_item_name;
        this.quantity = quantity;
        this.date_time = date_time;
        this.delivery_point = delivery_point;
        this.total_price = total_price;
        this.food_item_price_per_unit = food_item_price_per_unit;
    }

    public String getName() {
        return name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getFood_item_price_per_unit() {
        return food_item_price_per_unit;
    }

    public void setFood_item_price_per_unit(String food_item_price_per_unit) {
        this.food_item_price_per_unit = food_item_price_per_unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getFood_item_name() {
        return food_item_name;
    }

    public void setFood_item_name(String food_item_name) {
        this.food_item_name = food_item_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDelivery_point() {
        return delivery_point;
    }

    public void setDelivery_point(String delivery_point) {
        this.delivery_point = delivery_point;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
