
package model;

public abstract class Customer {
    protected String customerID;
    protected String name;
    protected String email;
    protected String address;
    protected String phone;
    protected Account account;

    // Overloaded constructor
    public Customer(String customerID, String email,
                    String phone, String address) {
        this.customerID = customerID;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Setter
    public void setAccount(Account account) {
        this.account = account;
    }

    // Getters
    public Account getAccount() {
        return account;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
