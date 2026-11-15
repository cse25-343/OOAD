public abstract class Customer {
    protected  String customerID;
    protected  String name;
    protected  String email;
    protected  String address;
    protected  String phone;
    protected Account account;

    //overloaded constructor
    public Customer(String customerID, String email,
                    String phone, String address) {
        this.customerID = customerID;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    //setters
    public void setAccount(Account account) {
        this.account = account;
    }

    //Getters
    public Account getAccount() {
        return account;
    }
}