package unit;

import checkout.component.AddState;
import checkout.component.dto.PurchaseRecord;
import checkout.component.dto.Receipt;
import checkout.component.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalculationTests {

    private CheckoutService checkoutService;

    @BeforeEach
    void setup() {
        checkoutService = new CheckoutService();
        checkoutService.setStarted(true);
        checkoutService.setSessionId("SESSION");
    }

    @Test
    void testSimpleCheckoutAndActualPrice() {
        assertEquals(AddState.ADDED, checkoutService.addItem("Test_A", 1), "Item was not added!");
        assertEquals(10f, checkoutService.actualPrice(), "Wrong calculated price!");
        assertEquals(AddState.ADDED, checkoutService.addItem("Test_B", 2), "Item was not added!");
        assertEquals(12f, checkoutService.actualPrice(), "Wrong calculated price!");
    }

    @Test
    void testDiscountAndReceipt() {
        assertEquals(AddState.ADDED, checkoutService.addItem("Test_A", 5), "Item was not added!");
        assertEquals(AddState.ADDED, checkoutService.addItem("Test_B", 9), "Item was not added!");
        Receipt receipt = checkoutService.receipt();
        assertNotNull(receipt, "Receipt not generated!");
        assertEquals(49f, receipt.getTotalPrice(), "Wrong price on receipt!");
        assertEquals(10f, receipt.getSavings(), "Wrong calculated savings!");
        assertTrue(receipt.getRecords().stream().anyMatch(
                record -> record.equals(new PurchaseRecord("Test_A", 5, 40f, 10f))),
                "Expected purchase record not found"
        );
        assertTrue(receipt.getRecords().stream().anyMatch(
                record -> record.equals(new PurchaseRecord("Test_B", 9, 9f, 0f))),
                "Expected purchase record not found"
        );
        assertEquals(2, receipt.getRecords().size(), "Unexpected number of purchase records");
    }

    @Test
    void testNotExistingItem() {
        assertEquals(AddState.UNKNOWN_ITEM, checkoutService.addItem("Unknown", 1), "Item was unexpectedly added!");
    }

    @Test
    void testCheckoutNotStarted() {
        checkoutService.setStarted(false);
        assertEquals(AddState.NOT_STARTED, checkoutService.addItem("Test_A", 1), "Item was unexpectedly added!");
    }
}
