package checkout.component.service;

import checkout.component.AddState;
import checkout.component.dto.Offer;
import checkout.component.dto.PurchaseRecord;
import checkout.component.dto.Receipt;
import checkout.component.dto.SpecialOffer;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@SessionScope
public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    @Setter
    private String sessionId;
    @Setter
    private boolean started;
    private final Map<String, Integer> items = new ConcurrentHashMap<>();

    public AddState addItem(String itemId, int quantity) {
        if (!started) {
            logger.warn("Checkout did not start");
            return AddState.NOT_STARTED;
        }
        if (Offer.getInstance().containsItem(itemId)) {
            items.put(itemId, items.getOrDefault(itemId, 0) + quantity);
            logger.info("Adding {} items with ID: {} to checkout (session ID: {})", quantity, itemId, sessionId);
            return AddState.ADDED;
        }
        return AddState.UNKNOWN_ITEM;
    }

    public float actualPrice() {
        float price = 0f;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            price += Offer.getInstance().getPrice(entry.getKey(), entry.getValue());
        }
        return price - calculateSpecialDiscount();
    }

    public Receipt receipt() {
        if (!started) {
            logger.warn("Checkout did not start, but trying to get a receipt");
            return null;
        }
        float savings = 0f;
        float totalPrice = 0f;
        Receipt.ReceiptBuilder receiptBuilder = Receipt.builder();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            float price = Offer.getInstance().getPrice(entry.getKey(), entry.getValue());
            float basePrice = Offer.getInstance().getPrice(entry.getKey(), 1) * entry.getValue();
            savings += (basePrice - price);
            totalPrice += price;
            receiptBuilder = receiptBuilder.record(new PurchaseRecord(entry.getKey(), entry.getValue(), price, basePrice - price));
        }
        totalPrice -= calculateSpecialDiscount();
        savings += calculateSpecialDiscount();
        return receiptBuilder.savings(savings).totalPrice(totalPrice).build();
    }

    private float calculateSpecialDiscount() {
        float discount = 0f;
        Map<String, Integer> copiedMap = new HashMap<>(items);
        List<SpecialOffer> specialOffers = Offer.getInstance().getSpecialOffers();
        for (SpecialOffer offer: specialOffers) {
            Map<String, Integer> removalMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry: offer.requirements().entrySet()) {
                if (entry.getValue() > copiedMap.getOrDefault(entry.getKey(), 0)) {
                    removalMap.put(entry.getKey(), -1);
                } else {
                    removalMap.put(entry.getKey(), entry.getValue());
                }
            }
            if (removalMap.values().stream().noneMatch(value -> value == -1)) {
                removalMap.forEach((key, value) -> copiedMap.put(key, copiedMap.get(key) - value));
                discount += offer.savings();
            }
        }
        return discount;
    }
}
