package checkout.component.rest;

import checkout.component.AddState;
import checkout.component.dto.Receipt;
import checkout.component.service.CheckoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("start")
    public ResponseEntity<Void> startCheckout(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        checkoutService.setSessionId(session.getId());
        checkoutService.setStarted(true);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("add")
    public ResponseEntity<Float> addItem(@RequestParam(name = "item_id") String itemId,
                                        @RequestParam(name = "quantity", defaultValue = "1") int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }
        AddState state = checkoutService.addItem(itemId, quantity);
        if (state == AddState.NOT_STARTED) {
            return ResponseEntity.badRequest().build();
        } else if (state == AddState.UNKNOWN_ITEM) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(checkoutService.actualPrice());
    }

    @GetMapping("receipt")
    public ResponseEntity<Receipt> getReceipt() {
        Receipt receipt = checkoutService.receipt();
        return ResponseEntity.status(receipt == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK).body(receipt);
    }
}
