package transactions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionRequest {

    private double amount;
    private String type;

    @JsonProperty("parent_id")
    private Long parentId;

    // Constructor vacío obligatorio (Jackson lo necesita para deserializar)
    public TransactionRequest() {}

    public double getAmount() { return amount; }
    public String getType() { return type; }
    public Long getParentId() { return parentId; }
}