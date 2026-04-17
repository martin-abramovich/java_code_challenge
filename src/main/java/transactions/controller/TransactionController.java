package transactions.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transactions.dto.TransactionRequest;
import transactions.service.TransactionService;

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
            @RequestBody TransactionRequest request) {

        service.save(id, request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}