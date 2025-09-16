package checkout.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public record Item(@JsonProperty("id") String itemId, @JsonProperty float price, @JsonProperty Discount discount) {
}
