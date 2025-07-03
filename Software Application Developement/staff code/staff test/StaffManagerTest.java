package catering.businesslogic.staff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StaffManagerTest {
    private StaffManager manager;
    private StaffMember robert;

    @BeforeEach
    public void setUp() {
        manager = new StaffManager();

        // Register staff
        robert = manager.registerStaff(
                "Robert", "Smith", "robert@example.com", "123456789", "RS123456", "permanent");

        manager.addAvailability(robert, LocalDate.of(2025, 6, 1), "service");
    }

    @Test
    public void testAssignStaffFlow() {
        // Step 1: Get available staff
        List<StaffMember> available = manager.getAvailablePermanentStaff(LocalDate.of(2025, 6, 1));
        assertTrue(available.contains(robert));

        // Step 2: Assign to a role
        StaffAssignment assignment = manager.assignStaffToRole(
                robert, "Waiter", "Sala", LocalDate.of(2025, 5, 28));
        assertEquals("pending", assignment.getResponseStatus());

        // Step 3: Process leave request (to test full version)
        LeaveRequest leave = manager.processLeaveRequest(robert, LocalDate.of(2025, 6, 10), LocalDate.of(2025, 6, 15));
        assertEquals("pending", leave.getStatus());

        // Step 4: Approve leave
        manager.updateLeaveRequestStatus(leave, "approved");
        assertEquals("approved", leave.getStatus());

        // Step 5: Verify assignment history
        long count = manager.getAssignmentCount(robert);
        assertEquals(1, count);
    }

    @Test
    public void testStaffDeclinesAssignment() {
        // Step 1: Register staff and set availability
        StaffMember anna = manager.registerStaff(
                "Anna", "Lee", "anna@example.com", "987654321", "AL987654", "occasional");
        manager.addAvailability(anna, LocalDate.of(2025, 6, 2), "service");

        // Step 2: Assign to role
        StaffAssignment assignment = manager.assignStaffToRole(
                anna, "Waiter", "Hall", LocalDate.of(2025, 5, 30));

        // Step 3: Staff declines the role
        assignment.setResponseStatus("declined");

        // Step 4: System should register that and allow reassignment (simulated here by checking status)
        assertEquals("declined", assignment.getResponseStatus());

        // Optional: show that the system could look for another staff now
        List<StaffMember> available = manager.getAvailablePermanentStaff(LocalDate.of(2025, 6, 2));
        assertFalse(available.contains(anna)); // she's occasional, not permanent

        System.out.println("Staff declined assignment: reassignment needed.");
    }

    @Test
    public void testUpdateMissingContactInfo() {
        StaffMember sara = manager.registerStaff(
                "Sara", "Neri", "", "", "SN554433", "occasional");

        assertEquals("", sara.getEmail());
        assertEquals("", sara.getPhone());

        manager.updateStaffContact(sara, "sara@example.com", "3344556677");

        assertEquals("sara@example.com", sara.getEmail());
        assertEquals("3344556677", sara.getPhone());

        System.out.println("Updated contact info for Sara.");
    }

    @Test
    public void testFrequentRecallSuggestsPermanentHire() {
        StaffMember marco = manager.registerStaff(
                "Marco", "Verdi", "marco@example.com", "2233445566", "MV998877", "occasional");

        // Simulate multiple assignments
        for (int i = 0; i < 5; i++) {
            manager.assignStaffToRole(marco, "Waiter", "Hall", LocalDate.now().plusDays(i));
        }

        long count = manager.getAssignmentCount(marco);

        assertTrue(count >= 5); // arbitrary threshold
        System.out.println("Marco has " + count + " assignments — consider permanent hire.");
    }

}
