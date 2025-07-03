package catering.businesslogic.staff;

import java.time.LocalDate;

public class StaffAssignment {
    private StaffMember staffMember;
    private String role;
    private String responseStatus;   // "pending", "accepted", "declined"
    private LocalDate responseDeadline;
    private String position;

    public StaffAssignment(StaffMember staffMember, String role, String responseStatus, LocalDate responseDeadline, String position) {
        this.staffMember = staffMember;
        this.role = role;
        this.responseStatus = responseStatus;
        this.responseDeadline = responseDeadline;
        this.position = position;
    }

    // Getters and setters
    public StaffMember getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(StaffMember staffMember) {
        this.staffMember = staffMember;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public LocalDate getResponseDeadline() {
        return responseDeadline;
    }

    public void setResponseDeadline(LocalDate responseDeadline) {
        this.responseDeadline = responseDeadline;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return staffMember + " assigned as " + position + " for role " + role +
                " (status: " + responseStatus + ", reply by: " + responseDeadline + ")";
    }
}
