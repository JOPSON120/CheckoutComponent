package checkout.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@JsonSerialize
@Builder
public class Receipt {
    @JsonProperty
    @Singular
    private List<PurchaseRecord> records;
    @JsonProperty("total_price")
    private float totalPrice;
    @JsonProperty
    private float savings;
}
