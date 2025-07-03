package catering.businesslogic.staff;

import catering.persistence.PersistenceManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Availability {
    private String staffTaxCode;
    private LocalDate date;
    private boolean isAvailable;

    // Constructors
    public Availability() {}

    public Availability(String staffTaxCode, LocalDate date, boolean isAvailable) {
        this.staffTaxCode = staffTaxCode;
        this.date = date;
        this.isAvailable = isAvailable;
    }

    // Getters
    public String getStaffTaxCode() {
        return staffTaxCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters
    public void setStaffTaxCode(String staffTaxCode) {
        this.staffTaxCode = staffTaxCode;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void save() throws SQLException {
        Connection conn = PersistenceManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Availabilities (staff_tax_code, date, is_available) VALUES (?, ?, ?)"
        );
        stmt.setString(1, this.staffTaxCode);
        stmt.setString(2, this.date.toString());
        stmt.setBoolean(3, this.isAvailable);
        stmt.executeUpdate();
        stmt.close();
    }

    public static List<Availability> loadByStaffTaxCode(String taxCode) throws SQLException {
        Connection conn = PersistenceManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Availabilities WHERE staff_tax_code = ?"
        );
        stmt.setString(1, taxCode);
        ResultSet rs = stmt.executeQuery();

        List<Availability> list = new ArrayList<>();
        while (rs.next()) {
            Availability a = new Availability();
            a.setStaffTaxCode(rs.getString("staff_tax_code"));
            a.setDate(LocalDate.parse(rs.getString("date")));
            a.setAvailable(rs.getBoolean("is_available"));
            list.add(a);
        }

        rs.close();
        stmt.close();
        return list;
    }
}
