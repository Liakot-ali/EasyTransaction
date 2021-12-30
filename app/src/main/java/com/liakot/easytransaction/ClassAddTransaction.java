package com.liakot.easytransaction;

public class ClassAddTransaction {

    long transactionNo;
    String date, explanation;
    long phone, expense, getMoney, remain;

    public ClassAddTransaction(String date, String explanation, long phone, long expense, long getMoney, long remain, String type) {
        this.date = date;
        this.explanation = explanation;
        this.phone = phone;
        this.expense = expense;
        this.getMoney = getMoney;
        this.remain = remain;
    }

    public ClassAddTransaction(long transactionNo, String date, String explanation, long phone, long expense, long getMoney, long remain) {
        this.transactionNo = transactionNo;
        this.date = date;
        this.explanation = explanation;
        this.phone = phone;
        this.expense = expense;
        this.getMoney = getMoney;
        this.remain = remain;
    }

    public long getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(long transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getExpense() {
        return expense;
    }

    public void setExpense(long expense) {
        this.expense = expense;
    }

    public long getGetMoney() {
        return getMoney;
    }

    public void setGetMoney(long getMoney) {
        this.getMoney = getMoney;
    }

    public long getRemain() {
        return remain;
    }

    public void setRemain(long remain) {
        this.remain = remain;
    }
}
