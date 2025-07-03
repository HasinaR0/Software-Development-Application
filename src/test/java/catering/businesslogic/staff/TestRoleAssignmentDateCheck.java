package catering.businesslogic.staff;

import catering.businesslogic.CatERing;
import catering.persistence.PersistenceManager;
import catering.util.LogManager;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRoleAssignmentDateCheck {

    private static final Logger LOGGER = LogManager.getLogger(TestRoleAssignmentDateCheck.class);
    private static StaffManager staffManager;
    private static final String TAX_CODE = "ROLE_TEST_001";

    @BeforeAll
    static void init() {
        LOGGER.info("🔧 Initializing system for StaffAssignmentDateConflictTest...");
        PersistenceManager.initializeDatabase("database/catering_init_sqlite.sql");
        CatERing.getInstance();
        staffManager = new StaffManager();
    }

    @Test
    @Order(1)
    void testRoleAssignmentDateCheck() throws SQLException {
        LOGGER.info("👤 Ensuring test staff is available...");

        StaffMember staff = StaffMember.loadByTaxCode(TAX_CODE);
        if (staff == null) {
            staff = staffManager.registerStaff(
                    "Giulia", "Test", "giulia@example.com", "5555555555", TAX_CODE, "permanent"
            );
            LOGGER.info("✅ Registered new staff: " + staff.getFirstName() + " " + staff.getLastName());
        } else {
            LOGGER.info("ℹ️ Using existing staff: " + staff.getFirstName() + " " + staff.getLastName());
        }

        LocalDate date1 = LocalDate.of(2025, 7, 1);
        LocalDate date2 = LocalDate.of(2025, 7, 2);

        LOGGER.info("📝 Assigning to role on first date: " + date1);
        StaffAssignment first = staffManager.assignStaffToRole(staff, "Cook", "Main Kitchen", date1);
        assertNotNull(first);

        LOGGER.info("❌ Attempting to assign same staff on the same date to a different role...");
        StaffAssignment conflict = staffManager.assignStaffToRole(staff, "Waiter", "Buffet", date1);
        assertNull(conflict);

        LOGGER.info("✅ Assigning same staff on a different date: " + date2);
        StaffAssignment second = staffManager.assignStaffToRole(staff, "Waiter", "Buffet", date2);
        assertNotNull(second);

        LOGGER.info("📋 Listing all assignments for this staff:");
        try {
            List<StaffAssignment> allAssignments = StaffAssignment.loadAll();
            for (StaffAssignment a : allAssignments) {
                if (TAX_CODE.equals(a.getStaffTaxCode())) {
                    LOGGER.info("   - " + a.getRoleName() + " at " + a.getPosition() + " on " + a.getDate());
                }
            }
        } catch (Exception e) {
            fail("❌ Could not load assignments: " + e.getMessage());
        }
    }
}
