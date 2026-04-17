package transactions.repository;

import org.springframework.stereotype.Repository;
import transactions.model.Transaction;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final ConcurrentHashMap<Long, Transaction> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Transaction transaction) {
        storage.put(transaction.getTransactionId(), transaction);
    }

    @Override
    public Optional<Transaction> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }
}