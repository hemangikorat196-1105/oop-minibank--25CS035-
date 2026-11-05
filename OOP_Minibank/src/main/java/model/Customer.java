package model;
import java.io.Serializable;
import model.annotation.Id;
import model.annotation.MaxLength;

public class Customer implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private static long customerCounter = 100;
    @Id private final String customerId;
    @MaxLength(60) private String name;
    private String email;
    private String mobile;
    private Address address;

    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId(); this.name = name; this.email = email; this.mobile = mobile;
    }
    private static synchronized String generateCustomerId() { return "CUST" + (++customerCounter); }
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    @Override public Customer clone() {
        try { Customer copy = (Customer) super.clone(); if (address != null) copy.address = address.clone(); return copy; }
        catch (CloneNotSupportedException e) { throw new AssertionError(e); }
    }
    @Override public String toString() { return "Customer{" + customerId + ", " + name + ", " + email + ", " + mobile + "}"; }
    @Override public boolean equals(Object o) { return o instanceof Customer c && customerId.equals(c.customerId); }
    @Override public int hashCode() { return customerId.hashCode(); }

    public static class Address implements Serializable, Cloneable {
        private static final long serialVersionUID = 1L;
        private String line;
        private String city;
        private String pincode;
        public Address(String line, String city, String pincode) { this.line = line; this.city = city; this.pincode = pincode; }
        public String getLine() { return line; }
        public String getCity() { return city; }
        public String getPincode() { return pincode; }
        @Override public Address clone() { try { return (Address) super.clone(); } catch (CloneNotSupportedException e) { throw new AssertionError(e); } }
        @Override public String toString() { return line + ", " + city + " - " + pincode; }
    }
}
