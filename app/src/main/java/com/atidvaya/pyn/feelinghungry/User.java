package com.atidvaya.pyn.feelinghungry;

/**
 * Created by pyn on 15-02-2016.
 */
public class User
{
    String name;
    String phone_number;

    public User(String name,String phone_number)

    {
        this.name=name;
        this.phone_number=phone_number;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
