package catering.businesslogic.staff;

import catering.persistence.PersistenceManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffMember {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String taxCode;
    private String employmentType; // "permanent" or "occasional"

    // Constructors
    public StaffMember() {
    }

    public StaffMember(String firstName, String lastName, String email, String phone, String taxCode, String employmentType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.taxCode = taxCode;
        this.employmentType = employmentType;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getTaxCode() { return taxCode; }
    public String getEmploymentType() { return employmentType; }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public void delete() throws SQLException {
        Connection conn = PersistenceManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM StaffMembers WHERE tax_code = ?");
        stmt.setString(1, this.taxCode);
        stmt.executeUpdate();
        stmt.close();
    }


    // Save to DB
    public void save() throws SQLException {
        Connection conn = PersistenceManager.getConnection();

        // Try to UPDATE first; if no rows affected, INSERT instead
        PreparedStatement updateStmt = conn.prepareStatement(
                "UPDATE StaffMembers SET first_name = ?, last_name = ?, email = ?, phone = ?, employment_type = ? WHERE tax_code = ?"
        );
        updateStmt.setString(1, this.firstName);
        updateStmt.setString(2, this.lastName);
        updateStmt.setString(3, this.email);
        updateStmt.setString(4, this.phone);
        updateStmt.setString(5, this.employmentType);
        updateStmt.setString(6, this.taxCode);
        int updated = updateStmt.executeUpdate();
        updateStmt.close();

        if (updated == 0) {
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO StaffMembers (tax_code, first_name, last_name, email, phone, employment_type) VALUES (?, ?, ?, ?, ?, ?)"
            );
            insertStmt.setString(1, this.taxCode);
            insertStmt.setString(2, this.firstName);
            insertStmt.setString(3, this.lastName);
            insertStmt.setString(4, this.email);
            insertStmt.setString(5, this.phone);
            insertStmt.setString(6, this.employmentType);
            insertStmt.executeUpdate();
            insertStmt.close();
        }
    }


    // Load staff by tax code
    public static StaffMember loadByTaxCode(String taxCode) throws SQLException {
        Connection conn = PersistenceManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM StaffMembers WHERE tax_code = ?"
        );
        stmt.setString(1, taxCode);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            StaffMember s = new StaffMember();
            s.setFirstName(rs.getString("first_name"));
            s.setLastName(rs.getString("last_name"));
            s.setEmail(rs.getString("email"));
            s.setPhone(rs.getString("phone"));
            s.setTaxCode(rs.getString("tax_code"));
            s.setEmploymentType(rs.getString("employment_type"));
            rs.close();
            stmt.close();
            return s;
        }

        rs.close();
        stmt.close();
        return null;
    }

    // Load all staff members
    public static List<StaffMember> loadAll() throws SQLException {
        Connection conn = PersistenceManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM StaffMembers");

        List<StaffMember> staffList = new ArrayList<>();
        while (rs.next()) {
            StaffMember s = new StaffMember();
            s.setFirstName(rs.getString("first_name"));
            s.setLastName(rs.getString("last_name"));
            s.setEmail(rs.getString("email"));
            s.setPhone(rs.getString("phone"));
            s.setTaxCode(rs.getString("tax_code"));
            s.setEmploymentType(rs.getString("employment_type"));
            staffList.add(s);
        }

        rs.close();
        stmt.close();
        return staffList;
    }
}
