package catering.businesslogic.staff;

import catering.persistence.PersistenceManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequest {
    private String staffTaxCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // e.g., "approved", "pending", "denied"

    // Constructors
    public LeaveRequest() {}

    public LeaveRequest(String staffTaxCode, LocalDate startDate, LocalDate endDate, String status) {
        this.staffTaxCode = staffTaxCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters
    public String getStaffTaxCode() {
        return staffTaxCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setStaffTaxCode(String staffTaxCode) {
        this.staffTaxCode = staffTaxCode;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Save to DB
    public void save() throws SQLException {
        Connection conn = PersistenceManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO LeaveRequests (staff_tax_code, start_date, end_date, status) VALUES (?, ?, ?, ?)"
        );
        stmt.setString(1, this.staffTaxCode);
        stmt.setString(2, this.startDate.toString());
        stmt.setString(3, this.endDate.toString());
        stmt.setString(4, this.status);
        stmt.executeUpdate();
        stmt.close();
    }

    public static List<LeaveRequest> loadByStaffTaxCode(String taxCode) throws SQLException {
        Connection conn = PersistenceManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM LeaveRequests WHERE staff_tax_code = ?"
        );
        stmt.setString(1, taxCode);
        ResultSet rs = stmt.executeQuery();

        List<LeaveRequest> list = new ArrayList<>();
        while (rs.next()) {
            LeaveRequest r = new LeaveRequest();
            r.setStaffTaxCode(rs.getString("staff_tax_code"));
            r.setStartDate(LocalDate.parse(rs.getString("start_date")));
            r.setEndDate(LocalDate.parse(rs.getString("end_date")));
            r.setStatus(rs.getString("status"));
            list.add(r);
        }

        rs.close();
        stmt.close();
        return list;
    }
}
