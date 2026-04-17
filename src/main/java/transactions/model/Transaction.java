package transactions.model;

public class Transaction {

    private long transactionId;
    private double amount;
    private String type;
    private Long parentId;

    public Transaction(long transactionId, double amount, String type, Long parentId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    public long getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public Long getParentId() { return parentId; }
}
