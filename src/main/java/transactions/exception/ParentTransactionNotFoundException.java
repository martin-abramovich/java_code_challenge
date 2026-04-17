package transactions.exception;

public class ParentTransactionNotFoundException extends RuntimeException {
    public ParentTransactionNotFoundException(long parentId) {
        super("Parent transaction not found: " + parentId);
    }
}