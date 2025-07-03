package catering.businesslogic.staff;

import catering.businesslogic.CatERing;
import catering.persistence.PersistenceManager;
import catering.util.LogManager;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterStaffDuplicateTaxCodeTest {

    private static final Logger LOGGER = LogManager.getLogger(RegisterStaffDuplicateTaxCodeTest.class);
    private static StaffManager staffManager;

    private static final String TEST_TAX_CODE = "DUP12345";

    @BeforeAll
    static void init() {
        LOGGER.info("🔧 Initializing database and system");
        PersistenceManager.initializeDatabase("database/catering_init_sqlite.sql");
        CatERing.getInstance();
        staffManager = new StaffManager();
    }

    /**
     * DEEP TEST 1: Registering Duplicate Staff Tax Code
     *
     * First run: creates the staff → OK
     * Second run: tries to create again → system throws exception
     */
    @Test
    @Order(1)
    void testRegisterStaffDuplicateTaxCode() {
        LOGGER.info("🔁 Attempting to register staff with tax code: " + TEST_TAX_CODE);

        StaffMember existing = null;
        try {
            existing = StaffMember.loadByTaxCode(TEST_TAX_CODE);
        } catch (Exception e) {
            LOGGER.warning("⚠️ Could not check existing staff: " + e.getMessage());
        }

        if (existing != null) {
            LOGGER.warning("⚠️ Staff already exists, registering again to trigger exception...");
        } else {
            LOGGER.info("✅ No existing staff — creating first time");
        }

        // This may throw an exception (on second run)
        StaffMember s = staffManager.registerStaff(
                "Laura", "Bianchi", "laura@example.com", "1111111111", TEST_TAX_CODE, "permanent"
        );

        LOGGER.info("✅ Staff registered: " + s.getFirstName() + " " + s.getLastName());

        LOGGER.info("📋 Current DB staff with this tax code:");
        try {
            List<StaffMember> all = StaffMember.loadAll();
            for (StaffMember st : all) {
                if (st.getTaxCode().equals(TEST_TAX_CODE)) {
                    LOGGER.info("🔸 " + st.getFirstName() + " " + st.getLastName() + " (" + st.getEmploymentType() + ")");
                }
            }
        } catch (Exception e) {
            LOGGER.warning("❌ Failed to load staff list: " + e.getMessage());
        }
    }

    @Test
    @Disabled("❌ Only enable if you want to manually delete test staff from the DB.")
    void deleteDuplicateTaxCodeStaff() {
        LOGGER.info("🧨 Deleting test staff with tax code: " + TEST_TAX_CODE);

        try {
            StaffMember s = StaffMember.loadByTaxCode(TEST_TAX_CODE);
            if (s != null) {
                s.delete(); // must be implemented in StaffMember.java
                LOGGER.info("✅ Deleted staff: " + s.getFirstName() + " " + s.getLastName());
            } else {
                LOGGER.info("ℹ️ No staff found with that tax code.");
            }
        } catch (Exception e) {
            LOGGER.warning("❌ Failed to delete staff: " + e.getMessage());
        }
    }
    


}
