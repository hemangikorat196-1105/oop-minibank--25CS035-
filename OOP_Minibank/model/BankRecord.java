package model;

import java.io.Serializable;

public class BankRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private int accountNumber;
    private String holderName;
    private transient String password;

    public BankRecord(int accountNumber, String holderName, String password) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + ", Holder Name: " + holderName
                + ", Password: " + password;
    }
}