package transactions.repository;

import transactions.model.Transaction;
import java.util.Optional;
import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);
    Optional<Transaction> findById(long id);
    List<Long> findIdsByType(String type);
}