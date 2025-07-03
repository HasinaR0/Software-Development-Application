
package catering.businesslogic.staff;

import catering.businesslogic.CatERing;
import catering.persistence.PersistenceManager;
import catering.util.LogManager;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GeneralFunctionalityTest {

    private static final Logger LOGGER = LogManager.getLogger(GeneralFunctionalityTest.class);
    private static StaffManager manager;
    private static final String TAX_CODE = "GENERAL_TEST_001";

    @BeforeAll
    static void init() {
        LOGGER.info("🔧 Initializing system for general method tests...");
        PersistenceManager.initializeDatabase("database/catering_init_sqlite.sql");
        CatERing.getInstance();
        manager = new StaffManager();
    }

    @Test
    @Order(1)
    void testRegisterStaffAndUpdateContact() throws Exception {
        LOGGER.info("────────────────────────────────────────────────────────────");
        LOGGER.info("🧪 STARTING: testRegisterStaffAndUpdateContact");

        StaffMember staff = StaffMember.loadByTaxCode(TAX_CODE);
        if (staff == null) {
            staff = manager.registerStaff("Elisa", "Moretti", "elisa@old.com", "999999999", TAX_CODE, "occasional");
            LOGGER.info("✅ Registered new staff: " + staff.getFirstName() + " " + staff.getLastName());
        } else {
            LOGGER.info("ℹ️ Staff already exists: " + staff.getFirstName() + " " + staff.getLastName());
        }

        LOGGER.info("📞 Current contact info: " + staff.getEmail() + ", " + staff.getPhone());
        LOGGER.info("🔄 Changing contact info...");
        manager.updateStaffContact(staff, "elisa@new.com", "111111111");
        LOGGER.info("✅ New contact info: " + staff.getEmail() + ", " + staff.getPhone());
    }

    @Test
    @Order(2)
    void testAddAvailability() throws Exception {
        LOGGER.info("────────────────────────────────────────────────────────────");
        LOGGER.info("🧪 STARTING: testAddAvailability");

        StaffMember staff = StaffMember.loadByTaxCode(TAX_CODE);
        assertNotNull(staff);
        LocalDate date = LocalDate.of(2025, 7, 3);

        LOGGER.info("🔍 Checking if staff is already available on: " + date);
        List<Availability> current = Availability.loadByStaffTaxCode(TAX_CODE);
        boolean isAvailable = current.stream().anyMatch(a -> a.getDate().equals(date) && a.isAvailable());
        LOGGER.info("📊 Initial availability on " + date + ": " + (isAvailable ? "Available" : "Not available"));

        manager.addAvailability(staff, date, "available");

        current = Availability.loadByStaffTaxCode(TAX_CODE);
        boolean nowAvailable = current.stream().anyMatch(a -> a.getDate().equals(date) && a.isAvailable());
        LOGGER.info("✅ Availability after update: " + (nowAvailable ? "Available" : "Not available"));
    }

    @Test
    @Order(3)
    void testGetAvailablePermanentStaff() throws Exception {
        LOGGER.info("────────────────────────────────────────────────────────────");
        LOGGER.info("🧪 STARTING: testGetAvailablePermanentStaff");

        // Simulate 2 permanent staff with availability
        StaffMember s1 = manager.registerStaff("Luca", "Bianchi", "luca@example.com", "123123123", "PERM001", "permanent");
        StaffMember s2 = manager.registerStaff("Marta", "Verdi", "marta@example.com", "456456456", "PERM002", "permanent");
        LocalDate date = LocalDate.of(2025, 7, 3);
        manager.addAvailability(s1, date, "available");
        manager.addAvailability(s2, date, "available");

        LOGGER.info("🔍 Querying available permanent staff for date: " + date);
        List<StaffMember> available = manager.getAvailablePermanentStaff(date);
        LOGGER.info("📊 Found " + available.size() + " available permanent staff:");
        for (StaffMember s : available) {
            LOGGER.info("   - " + s.getFirstName() + " " + s.getLastName());
        }
    }

    @Test
    @Order(4)
    void testProcessLeaveRequest() throws Exception {
        LOGGER.info("────────────────────────────────────────────────────────────");
        LOGGER.info("🧪 STARTING: testProcessLeaveRequest");

        StaffMember staff = StaffMember.loadByTaxCode(TAX_CODE);
        assertNotNull(staff);

        LocalDate start = LocalDate.of(2025, 7, 10);
        LocalDate end = LocalDate.of(2025, 7, 12);

        LOGGER.info("📤 Processing leave request for " + staff.getFirstName() + " from " + start + " to " + end);
        LeaveRequest leave = manager.processLeaveRequest(staff, start, end);
        LOGGER.info("✅ Leave request saved with status: " + leave.getStatus());
    }

    @Test
    @Order(5)
    void testSuggestPermanentHire() throws Exception {
        LOGGER.info("────────────────────────────────────────────────────────────");
        LOGGER.info("🧪 STARTING: testSuggestPermanentHire");

        // Add 3 occasional staff, simulate 1 being very active
        StaffMember a = manager.registerStaff("Simone", "Test1", "a@occasional.com", "101010", "OCC001", "occasional");
        StaffMember b = manager.registerStaff("Francesca", "Test2", "b@occasional.com", "202020", "OCC002", "occasional");
        StaffMember c = manager.registerStaff("Marco", "Test3", "c@occasional.com", "303030", "OCC003", "occasional");

        LocalDate date = LocalDate.of(2025, 7, 15);
        manager.assignStaffToRole(a, "Cook", "Main Kitchen", date);
        manager.assignStaffToRole(a, "Waiter", "Buffet", date.plusDays(1));
        manager.assignStaffToRole(a, "Cleaner", "Backroom", date.plusDays(2)); // more active
        manager.assignStaffToRole(b, "Cook", "Side Kitchen", date);
        manager.assignStaffToRole(c, "Waiter", "Buffet", date);

        LOGGER.info("📊 Assignment counts:");
        LOGGER.info("   - Simone has 3 assignments");
        LOGGER.info("   - Francesca has 1 assignment");
        LOGGER.info("   - Marco has 1 assignment");

        LOGGER.info("💡 Suggesting Simone for permanent hire...");
        manager.suggestPermanentHire(a);
    }

}
