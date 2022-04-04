package com.liakot.easytransaction;


public class ClassAddCustomer {
    String name, address;
    byte[] picture;
    long id, phone, amount;
    String type;



    public ClassAddCustomer(String name, long phone, String address, byte[] picture, long amount) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.picture = picture;
        this.amount = amount;
    }

    public ClassAddCustomer(long id, String name, long phone, String address, byte[] picture, long amount) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.picture = picture;
        this.amount = amount;
    }

    public ClassAddCustomer(String name, String address, byte[] picture, long id, long phone, long amount, String type) {
        this.name = name;
        this.address = address;
        this.picture = picture;
        this.id = id;
        this.phone = phone;
        this.amount = amount;
        this.type = type;
    }

    //---------For show the value together-------
    @Override
    public String toString() {
        return "ClassAddCustomer{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", picture='" + picture + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
