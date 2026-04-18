package transactions.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transactions.dto.SumResponse;
import transactions.dto.TransactionRequest;
import transactions.service.TransactionService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;

@Tag(name = "Transactions", description = "Transaction management API")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @Operation(summary = "Create or replace a transaction")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> createTransaction(
            @PathVariable long id,
            @Valid @RequestBody TransactionRequest request) {

        service.save(id, request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @Operation(summary = "Get all transaction IDs by type")
    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getTransactionsByType(@PathVariable String type) {
        return ResponseEntity.ok(service.findIdsByType(type));
    }

    @Operation(summary = "Get transitive sum of a transaction and all its descendants")
    @GetMapping("/sum/{id}")
    public ResponseEntity<SumResponse> getSum(@PathVariable long id) {
        return ResponseEntity.ok(new SumResponse(service.calculateSum(id)));
    }
}