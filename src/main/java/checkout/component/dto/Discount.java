package checkout.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public record Discount(@JsonProperty int quantity, @JsonProperty float price) {
}
