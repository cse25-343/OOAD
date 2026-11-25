
package model;

public class Company extends Customer {

    private String companyName;
    private String registrationNumber;

    public Company(String customerID, String email, String phone, String address,
                   String companyName, String registrationNumber) {
        super(customerID, email, phone, address);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
}
