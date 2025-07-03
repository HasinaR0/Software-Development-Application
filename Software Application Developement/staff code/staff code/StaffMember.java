package catering.businesslogic.staff;

public class StaffMember {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String taxCode;
    private String employmentType; // "permanent" or "occasional"

    public StaffMember(String firstName, String lastName, String email, String phone, String taxCode, String employmentType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.taxCode = taxCode;
        this.employmentType = employmentType;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + employmentType + ")";
    }
}
