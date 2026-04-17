package transactions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransactionRequest {

    @NotNull(message = "amount is required")
    private Double amount;

    @NotBlank(message = "type is required")
    private String type;

    @JsonProperty("parent_id")
    private Long parentId;

    // Constructor vacío obligatorio (Jackson lo necesita para deserializar)
    public TransactionRequest() {}

    public double getAmount() { return amount; }
    public String getType() { return type; }
    public Long getParentId() { return parentId; }
}