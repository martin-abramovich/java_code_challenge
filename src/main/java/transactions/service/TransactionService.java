package transactions.service;

import org.springframework.stereotype.Service;
import transactions.dto.TransactionRequest;
import transactions.exception.ParentTransactionNotFoundException;
import transactions.exception.TransactionNotFoundException;
import transactions.model.Transaction;
import transactions.repository.TransactionRepository;
import java.util.List;


@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void save(long transactionId, TransactionRequest request) {
        if (request.getParentId() != null) {
            repository.findById(request.getParentId())
                    .orElseThrow(() -> new ParentTransactionNotFoundException(request.getParentId()));
        }

        Transaction transaction = new Transaction(
                transactionId,
                request.getAmount(),
                request.getType(),
                request.getParentId()
        );
        repository.save(transaction);
    }
    public List<Long> findIdsByType(String type) {
        return repository.findIdsByType(type);
    }

    public double calculateSum(long transactionId) {
        repository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        return 0.0; // mínimo para que compile
    }
}