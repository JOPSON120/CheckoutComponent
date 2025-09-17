package checkout.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

@JsonDeserialize
public record SpecialOffer(@JsonProperty Map<String, Integer> requirements, @JsonProperty float savings) {

}
