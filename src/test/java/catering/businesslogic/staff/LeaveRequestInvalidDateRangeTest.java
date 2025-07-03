package catering.businesslogic.staff;

import catering.businesslogic.CatERing;
import catering.persistence.PersistenceManager;
import catering.util.LogManager;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LeaveRequestInvalidDateRangeTest {

    private static final Logger LOGGER = LogManager.getLogger(LeaveRequestInvalidDateRangeTest.class);
    private static StaffManager staffManager;

    private static final String TEST_TAX_CODE = "INVALID_LEAVE_001";

    @BeforeAll
    static void init() {
        LOGGER.info("🔧 Initializing system for LeaveRequestInvalidDateRangeTest");
        PersistenceManager.initializeDatabase("database/catering_init_sqlite.sql");
        CatERing.getInstance();
        staffManager = new StaffManager();
    }

    /**
     * DEEP TEST 2: Submitting a Leave Request with User-Inputted Date Range
     *
     * Reuses staff if already exists. Valid range saves successfully, invalid throws with log.
     */
    @Test
    @Order(1)
    void testLeaveRequestWithInputDateCheck() {
        LOGGER.info("👤 Preparing staff with tax code: " + TEST_TAX_CODE);
        StaffMember staff;

        try {
            staff = StaffMember.loadByTaxCode(TEST_TAX_CODE);
            if (staff != null) {
                LOGGER.info("ℹ️ Staff already exists: " + staff.getFirstName() + " " + staff.getLastName());
            } else {
                LOGGER.info("🆕 Registering new occasional staff...");
                staff = staffManager.registerStaff(
                        "Fabio", "Rossi", "fabio@example.com", "4444444444", TEST_TAX_CODE, "occasional"
                );
                LOGGER.info("✅ Registered: " + staff.getFirstName() + " " + staff.getLastName());
            }
        } catch (Exception e) {
            fail("❌ Failed to load or register staff: " + e.getMessage());
            return;
        }

        // Simulated user input
        LocalDate start = LocalDate.of(2025, 8, 10);
        LocalDate end = LocalDate.of(2025, 8, 9); // change this to 2026 for valid test

        LOGGER.info("📥 User input: start = " + start + ", end = " + end);

        if (start.isAfter(end)) {
            String msg = "❌ Invalid date range: start date is after end date.";
            LOGGER.warning(msg);
            throw new IllegalArgumentException(msg);
        }

        try {
            LeaveRequest leave = new LeaveRequest(TEST_TAX_CODE, start, end, "pending");
            leave.save();
            LOGGER.info("✅ Leave request saved: " + start + " → " + end);
        } catch (Exception e) {
            fail("❌ Failed to save leave request: " + e.getMessage());
        }
    }
}
