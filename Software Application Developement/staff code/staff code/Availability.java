package catering.businesslogic.staff;

import java.time.LocalDate;

public class Availability {
    private StaffMember staffMember;
    private LocalDate availableDate;
    private String availableType; // "service" or "preparation"

    public Availability(StaffMember staffMember, LocalDate availableDate, String availableType) {
        this.staffMember = staffMember;
        this.availableDate = availableDate;
        this.availableType = availableType;
    }

    // Getters and setters
    public StaffMember getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(StaffMember staffMember) {
        this.staffMember = staffMember;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public String getAvailableType() {
        return availableType;
    }

    public void setAvailableType(String availableType) {
        this.availableType = availableType;
    }

    @Override
    public String toString() {
        return "Available on " + availableDate + " for " + availableType + " shift (" + staffMember + ")";
    }
}
