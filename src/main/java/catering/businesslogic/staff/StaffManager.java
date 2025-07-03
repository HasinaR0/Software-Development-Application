package catering.businesslogic.staff;

import catering.util.LogManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StaffManager {

    public StaffManager() {
        // Nothing to initialize — we use DB instead of in-memory lists
    }

    //  Register a new staff member (checks duplicate tax code)
    public StaffMember registerStaff(String firstName, String lastName, String email, String phone, String taxCode, String employmentType) {
        try {
            StaffMember existing = StaffMember.loadByTaxCode(taxCode);
            if (existing != null) {
                throw new IllegalArgumentException("❌ Staff with this tax code already exists.");
            }

            StaffMember s = new StaffMember(firstName, lastName, email, phone, taxCode, employmentType);
            s.save();
            return s;
        } catch (SQLException e) {
            throw new RuntimeException("💥 Error registering staff: " + e.getMessage(), e);
        }
    }

    //  Add availability
    public void addAvailability(StaffMember staff, LocalDate date, String type) {
        try {
            boolean isAvailable = type.equalsIgnoreCase("available");
            Availability availability = new Availability(staff.getTaxCode(), date, isAvailable);
            availability.save();
        } catch (SQLException e) {
            throw new RuntimeException("💥 Error saving availability: " + e.getMessage(), e);
        }
    }

    //  Assign staff to role
    public StaffAssignment assignStaffToRole(StaffMember staff, String role, String position, LocalDate deadline) {
        Logger LOGGER = LogManager.getLogger(StaffManager.class);
        int eventId = 0; // Replace if using real event IDs

        try {
            List<StaffAssignment> allAssignments = StaffAssignment.loadAll();

            for (StaffAssignment a : allAssignments) {
                String aTaxCode = a.getStaffTaxCode();
                LocalDate aDate = a.getDate();

                //  UC 7.1a: If already assigned on same date, block any further assignments
                if (
                        aTaxCode != null && aTaxCode.equals(staff.getTaxCode()) &&
                                aDate != null && aDate.equals(deadline)
                ) {
                    LOGGER.warning("⛔ Staff already assigned on " + deadline + " as " +
                            a.getRoleName() + " at " + a.getPosition() + " (event " + a.getEventId() + ")");
                    return null;
                }
            }
            StaffAssignment assignment = new StaffAssignment();
            assignment.setStaffTaxCode(staff.getTaxCode());
            assignment.setRoleName(role);
            assignment.setPosition(position);
            assignment.setDate(deadline);
            assignment.setEventId(eventId);

            assignment.save();
            LOGGER.info("✅ Staff assigned to " + role + " at " + position + " on " + deadline);
            return assignment;

        } catch (Exception e) {
            LOGGER.warning("⚠️ Failed to assign staff: " + e.getMessage());
            return null;
        }
    }

    //  Process leave request
    public LeaveRequest processLeaveRequest(StaffMember staff, LocalDate start, LocalDate end) {
        try {
            LeaveRequest r = new LeaveRequest(staff.getTaxCode(), start, end, "pending");
            r.save();
            return r;
        } catch (SQLException e) {
            throw new RuntimeException("💥 Error processing leave request: " + e.getMessage(), e);
        }
    }

    //  Update contact info
    public void updateStaffContact(StaffMember staff, String email, String phone) {
        staff.setEmail(email);
        staff.setPhone(phone);
        try {
            staff.save(); // simple overwrite
        } catch (SQLException e) {
            throw new RuntimeException("💥 Error updating contact info: " + e.getMessage(), e);
        }
    }

    //  Get all staff available on a given date
    public List<StaffMember> getAvailablePermanentStaff(LocalDate date) {
        try {
            List<StaffMember> all = StaffMember.loadAll();
            return all.stream()
                    .filter(s -> {
                        try {
                            if (!s.getEmploymentType().equalsIgnoreCase("permanent")) return false;
                            List<Availability> a = Availability.loadByStaffTaxCode(s.getTaxCode());
                            return a.stream().anyMatch(av -> av.getDate().equals(date) && av.isAvailable());
                        } catch (SQLException e) {
                            return false;
                        }
                    }).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("💥 Error loading staff list: " + e.getMessage(), e);
        }
    }

    public void suggestPermanentHire(StaffMember staff) {
        System.out.println("💡 Consider hiring this occasional staff permanently: " + staff.getFirstName() + " " + staff.getLastName());
    }
}
