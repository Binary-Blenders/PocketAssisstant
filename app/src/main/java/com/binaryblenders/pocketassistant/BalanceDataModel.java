package com.binaryblenders.pocketassistant;

public class BalanceDataModel {
    private long added;
    private long balance;
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getAdded() {
        return added;
    }

    public long getBalance() {
        return balance;
    }

    public BalanceDataModel() {
    }

    public BalanceDataModel(long added, long balance) {
        this.added = added;
        this.balance = balance;
    }
}
