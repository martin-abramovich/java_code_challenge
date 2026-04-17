package transactions.service;

import org.springframework.stereotype.Service;
import transactions.dto.TransactionRequest;
import transactions.model.Transaction;
import transactions.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void save(long transactionId, TransactionRequest request) {
        Transaction transaction = new Transaction(
                transactionId,
                request.getAmount(),
                request.getType(),
                request.getParentId()
        );
        repository.save(transaction);
    }
}