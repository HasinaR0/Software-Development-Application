package catering.businesslogic.staff;

import java.time.LocalDate;

public class LeaveRequest {
    private StaffMember staffMember;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // "pending", "approved", "denied"

    public LeaveRequest(StaffMember staffMember, LocalDate startDate, LocalDate endDate, String status) {
        this.staffMember = staffMember;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and setters
    public StaffMember getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(StaffMember staffMember) {
        this.staffMember = staffMember;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return staffMember + " requests leave from " + startDate + " to " + endDate + " [" + status + "]";
    }
}
