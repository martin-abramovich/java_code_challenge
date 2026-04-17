package transactions.repository;

import org.springframework.stereotype.Repository;
import transactions.model.Transaction;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Long> findIdsByType(String type) {
        return storage.values().stream()
                .filter(t -> t.getType().equals(type))
                .map(Transaction::getTransactionId)
                .collect(Collectors.toList());
    }
}