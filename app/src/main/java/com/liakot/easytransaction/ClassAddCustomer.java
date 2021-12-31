package com.liakot.easytransaction;

//---------Liakot Ali Liton, ID : 1802035----------

public class ClassAddCustomer {
    String name, address;
    byte[] picture;
    long phone, amount;

    public ClassAddCustomer(String name, long phone, String address, byte[] picture, long amount) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.picture = picture;
        this.amount = amount;
    }


    //---------For show the value together-------
    @Override
    public String toString() {
        return "ClassAddCustomer{" +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", picture='" + picture + '\'' +
                ", amount='" + amount + '\'' +
                '}';
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
}
