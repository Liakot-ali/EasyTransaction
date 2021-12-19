package com.liakot.easytransaction;

public class ClassAddCustomer {
    String type, name, phone, address, picture;

    public ClassAddCustomer(String type, String name, String phone, String address, String picture) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
