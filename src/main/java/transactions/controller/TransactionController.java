package transactions.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transactions.dto.SumResponse;
import transactions.dto.TransactionRequest;
import transactions.service.TransactionService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> createTransaction(
            @PathVariable long id,
            @Valid @RequestBody TransactionRequest request) {

        service.save(id, request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getTransactionsByType(@PathVariable String type) {
        return ResponseEntity.ok(service.findIdsByType(type));
    }

    @GetMapping("/sum/{id}")
    public ResponseEntity<SumResponse> getSum(@PathVariable long id) {
        return ResponseEntity.ok(new SumResponse(service.calculateSum(id)));
    }
}