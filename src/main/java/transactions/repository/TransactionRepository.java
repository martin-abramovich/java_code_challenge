package transactions.repository;

import transactions.model.Transaction;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);
    Optional<Transaction> findById(long id);
}