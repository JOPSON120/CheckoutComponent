package checkout.component.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Offer {

    @Getter(lazy = true)
    private static final Offer instance = new Offer();
    @Getter
    private final Map<String, Item> itemsMap;
    @Getter
    private final List<SpecialOffer> specialOffers;

    @SneakyThrows
    private Offer() {
        ObjectMapper mapper = new ObjectMapper();
        URL resource = ClassLoader.getSystemResource("items.json");
        URL specialResource = ClassLoader.getSystemResource("special_offers.json");
        List<Item> itemList = mapper.readValue(resource, mapper.getTypeFactory().constructCollectionType(List.class, Item.class));
        itemsMap = itemList.stream().collect(Collectors.toMap(Item::itemId, item -> item));
        if (specialResource == null) {
            specialOffers = new ArrayList<>();
        } else {
            specialOffers = mapper.readValue(specialResource, mapper.getTypeFactory().constructCollectionType(List.class, SpecialOffer.class));
        }
    }

    public boolean containsItem(String itemId) {
        return itemsMap.containsKey(itemId);
    }

    public float getPrice(String itemId, int quantity) {
        Item item = itemsMap.get(itemId);
        if (item.discount() == null) {
            return quantity * item.price();
        }
        int discounted = item.discount().quantity() * (quantity / item.discount().quantity());
        return discounted * item.discount().price() + (quantity - discounted) * item.price();
    }
}
