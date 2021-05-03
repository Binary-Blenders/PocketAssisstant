package com.binaryblenders.pocketassistant;

import com.google.firebase.Timestamp;

public class TransactionItems {
    private String amount;
    private String purpose;
    private String source;
    private String time;
    private String transaction_type;

    public TransactionItems(String amount, String purpose, String source, String time, String transaction_type) {
        this.amount = amount;
        this.purpose = purpose;
        this.source = source;
        this.time = time;
        this.transaction_type = transaction_type;
    }

    public String getAmount() {
        return amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getSource() {
        return source;
    }

    public String getTime() {
        return time;
    }

    public String getTransaction_type() {
        return transaction_type;
    }
}


