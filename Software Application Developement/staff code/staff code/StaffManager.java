package catering.businesslogic.staff;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StaffManager {

    private List<StaffMember> staffList;
    private List<Availability> availabilities;
    private List<LeaveRequest> leaveRequests;
    private List<StaffAssignment> assignments;

    public StaffManager() {
        this.staffList = new ArrayList<>();
        this.availabilities = new ArrayList<>();
        this.leaveRequests = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    // Register a new staff member
    public StaffMember registerStaff(String firstName, String lastName, String email, String phone, String taxCode, String employmentType) {
        StaffMember newStaff = new StaffMember(firstName, lastName, email, phone, taxCode, employmentType);
        staffList.add(newStaff);
        return newStaff;
    }

    // Get list of available permanent staff for a given date
    public List<StaffMember> getAvailablePermanentStaff(LocalDate date) {
        return staffList.stream()
                .filter(s -> s.getEmploymentType().equalsIgnoreCase("permanent"))
                .filter(s -> isAvailable(s, date))
                .collect(Collectors.toList());
    }

    // Check if a staff member is available on a given date
    private boolean isAvailable(StaffMember staff, LocalDate date) {
        return availabilities.stream()
                .anyMatch(a -> a.getStaffMember().equals(staff) && a.getAvailableDate().equals(date));
    }

    // Add availability for a staff member
    public void addAvailability(StaffMember staff, LocalDate date, String type) {
        availabilities.add(new Availability(staff, date, type));
    }

    // Assign a staff member to a role
    public StaffAssignment assignStaffToRole(StaffMember staff, String role, String position, LocalDate deadline) {
        StaffAssignment assignment = new StaffAssignment(staff, role, "pending", deadline, position);
        assignments.add(assignment);
        return assignment;
    }

    // Update staff contact
    public void updateStaffContact(StaffMember staff, String newEmail, String newPhone) {
        staff.setEmail(newEmail);
        staff.setPhone(newPhone);
    }

    // Submit a leave request
    public LeaveRequest processLeaveRequest(StaffMember staff, LocalDate start, LocalDate end) {
        LeaveRequest request = new LeaveRequest(staff, start, end, "pending");
        leaveRequests.add(request);
        return request;
    }

    // Approve or deny a leave request
    public void updateLeaveRequestStatus(LeaveRequest request, String status) {
        request.setStatus(status); // "approved" or "denied"
    }

    // Get staff history (e.g., number of assignments)
    public long getAssignmentCount(StaffMember staff) {
        return assignments.stream().filter(a -> a.getStaffMember().equals(staff)).count();
    }

    public List<StaffMember> getAllStaff() {
        return staffList;
    }

    public List<StaffAssignment> getAllAssignments() {
        return assignments;
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequests;
    }

    public List<Availability> getAllAvailabilities() {
        return availabilities;
    }
}
