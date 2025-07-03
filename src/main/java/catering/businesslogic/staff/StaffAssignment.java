package catering.businesslogic.staff;

import catering.persistence.PersistenceManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StaffAssignment {
    private String staffTaxCode;
    private int eventId;
    private String roleName;
    private LocalDate date;
    private String position;


    // Constructors
    public StaffAssignment() {}

    public StaffAssignment(String staffTaxCode, int eventId, String roleName, LocalDate date) {
        this.staffTaxCode = staffTaxCode;
        this.eventId = eventId;
        this.roleName = roleName;
        this.date = date;
    }

    public static List<StaffAssignment> loadAll() throws SQLException {
        String query = "SELECT * FROM StaffAssignments";
        PreparedStatement stmt = PersistenceManager.getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        List<StaffAssignment> assignments = new ArrayList<>();
        while (rs.next()) {
            StaffAssignment assignment = new StaffAssignment();
            assignment.setStaffTaxCode(rs.getString("staff_tax_code"));
            assignment.setRoleName(rs.getString("role_name"));
            assignment.setPosition(rs.getString("position"));
            Timestamp timestamp = rs.getTimestamp("date");
            assignment.setDate(timestamp.toLocalDateTime().toLocalDate());
            assignment.setEventId(rs.getInt("event_id"));
            assignments.add(assignment);
        }

        rs.close();
        stmt.close();
        return assignments;
    }


    // Getters
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStaffTaxCode() {
        return staffTaxCode;
    }

    public int getEventId() {
        return eventId;
    }

    public String getRoleName() {
        return roleName;
    }

    public LocalDate getDate() {
        return date;
    }

    // Setters
    public void setStaffTaxCode(String staffTaxCode) {
        this.staffTaxCode = staffTaxCode;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Save to DB
    public void save() throws SQLException {
        Connection conn = PersistenceManager.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO StaffAssignments (staff_tax_code, role_name, position, date, event_id) VALUES (?, ?, ?, ?, ?)"
        );

        stmt.setString(1, this.staffTaxCode);
        stmt.setString(2, this.roleName);
        stmt.setString(3, this.position);
        stmt.setDate(4, java.sql.Date.valueOf(this.date)); // ✅ This is correct for LocalDate
        stmt.setInt(5, this.eventId);

        stmt.executeUpdate();
        stmt.close();
    }

    public static List<StaffAssignment> loadByEvent(int eventId) throws SQLException {
        Connection conn = PersistenceManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM StaffAssignments WHERE event_id = ?"
        );
        stmt.setInt(1, eventId);
        ResultSet rs = stmt.executeQuery();

        List<StaffAssignment> list = new ArrayList<>();
        while (rs.next()) {
            StaffAssignment a = new StaffAssignment();
            a.setStaffTaxCode(rs.getString("staff_tax_code"));
            a.setEventId(rs.getInt("event_id"));
            a.setRoleName(rs.getString("role_name"));
            a.setDate(LocalDate.parse(rs.getString("date")));
            list.add(a);
        }

        rs.close();
        stmt.close();
        return list;
    }
}
