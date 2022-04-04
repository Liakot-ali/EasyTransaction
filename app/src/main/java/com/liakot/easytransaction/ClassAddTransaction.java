package com.liakot.easytransaction;


public class ClassAddTransaction {

    long transactionNo;
    String date, explanation, type;
    long customerId, expense, getMoney, remain;

    public ClassAddTransaction(String date, String explanation, long customerId, long expense, long getMoney, long remain, String type) {
        this.date = date;
        this.explanation = explanation;
        this.customerId = customerId;
        this.expense = expense;
        this.getMoney = getMoney;
        this.remain = remain;
        this.type = type;
    }

    public ClassAddTransaction(long transactionNo, String date, String explanation, long customerId, long expense, long getMoney, long remain, String  type) {
        this.transactionNo = transactionNo;
        this.date = date;
        this.explanation = explanation;
        this.customerId = customerId;
        this.expense = expense;
        this.getMoney = getMoney;
        this.remain = remain;
        this.type = type;
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

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
