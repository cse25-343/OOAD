
package model;

public class Person extends Customer {

    private String firstName;
    private String lastName;

    public Person(String customerID, String email, String phone, String address,
                  String firstName, String lastName) {
        super(customerID, email, phone, address);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
