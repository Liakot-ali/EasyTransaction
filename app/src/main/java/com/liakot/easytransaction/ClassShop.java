package com.liakot.easytransaction;

public class ClassShop {
    String name, owner, category, password, address;
    long phone, totalRemain, totalPayble, customerNo, paybleNo;
    byte[] picture;

    public ClassShop(String name, String owner, String category, String password, String address, long phone, long totalRemain, long totalPayble, long customerNo, long paybleNo, byte[] picture) {
        this.name = name;
        this.owner = owner;
        this.category = category;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.totalRemain = totalRemain;
        this.totalPayble = totalPayble;
        this.customerNo = customerNo;
        this.paybleNo = paybleNo;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getTotalRemain() {
        return totalRemain;
    }

    public void setTotalRemain(long totalRemain) {
        this.totalRemain = totalRemain;
    }

    public long getTotalPayble() {
        return totalPayble;
    }

    public void setTotalPayble(long totalPayble) {
        this.totalPayble = totalPayble;
    }

    public long getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(long customerNo) {
        this.customerNo = customerNo;
    }

    public long getPaybleNo() {
        return paybleNo;
    }

    public void setPaybleNo(long paybleNo) {
        this.paybleNo = paybleNo;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
