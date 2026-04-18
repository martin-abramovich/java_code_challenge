package transactions.exception;

public class CyclicTransactionException extends RuntimeException {
    public CyclicTransactionException(long id) {
        super("Transaction " + id + " would create a cycle");
    }
}