package transactions.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(long id) {
        super("Transaction not found: " + id);
    }
}