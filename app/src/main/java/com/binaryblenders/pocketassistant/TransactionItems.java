package com.binaryblenders.pocketassistant;

public class TransactionItems {
    private String purpose;
    private String rupees;
    private String time;
    private String reason;
    private String transactiontype;

    public TransactionItems(String rupees, String time, String reason, String transactiontype,String purpose) {
        this.rupees = rupees;
        this.time = time;
        this.reason = reason;
        this.transactiontype = transactiontype;
        this.purpose = purpose;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getRupees() {
        return rupees;
    }

    public String getTime() {
        return time;
    }
    public String getReason() {
        return reason;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

}


