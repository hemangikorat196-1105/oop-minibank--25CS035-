package model;

import java.io.Serializable;
import model.annotation.MaxLength;

public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private int customerId;

    @MaxLength(10)
    private String name;

    private String email;
    private Address address;

    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.address = new Address("", "", "");
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(String street, String city, String pinCode) {
        this.address = new Address(street, city, pinCode);
    }

    public void displayCustomer() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Address: " + address);
    }

    public class Address implements Serializable {

        private static final long serialVersionUID = 1L;

        private String street;
        private String city;
        private String pinCode;

        public Address(String street, String city, String pinCode) {
            this.street = street;
            this.city = city;
            this.pinCode = pinCode;
        }

        @Override
        public String toString() {
            return street + ", " + city + " - " + pinCode;
        }
    }
}