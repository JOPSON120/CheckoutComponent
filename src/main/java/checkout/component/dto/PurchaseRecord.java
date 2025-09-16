package checkout.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record PurchaseRecord (@JsonProperty("item_id") String itemId, @JsonProperty int quantity, @JsonProperty float price, @JsonProperty float savings){
}
