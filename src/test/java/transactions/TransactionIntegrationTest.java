package transactions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import transactions.repository.TransactionRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired
    TransactionRepository transactionRepository;

    @BeforeEach
    void cleanRepository() {
        transactionRepository.clear();
    }

    @Test
    void shouldCreateTransactionAndReturn200() throws Exception {
        String body = """
                {"amount": 5000.0, "type": "cars"}
                """;

        mockMvc.perform(put("/transactions/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void shouldReturn400WhenParentIdDoesNotExist() throws Exception {
        String body = """
            {"amount": 1000.0, "type": "food", "parent_id": 999}
            """;

        mockMvc.perform(put("/transactions/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateTransactionWithValidParentId() throws Exception {
        // Primero crear el padre
        mockMvc.perform(put("/transactions/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"amount": 5000.0, "type": "cars"}
                            """))
                .andExpect(status().isOk());

        // Luego el hijo con parent_id=10
        mockMvc.perform(put("/transactions/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"amount": 2000.0, "type": "shopping", "parent_id": 10}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void shouldReturn400WhenAmountIsMissing() throws Exception {
        mockMvc.perform(put("/transactions/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"type": "cars"}
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenTypeIsMissing() throws Exception {
        mockMvc.perform(put("/transactions/31")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"amount": 1000.0}
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAmountIsNegative() throws Exception {
        mockMvc.perform(put("/transactions/40")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"amount": -100.0, "type": "cars"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAmountIsZero() throws Exception {
        mockMvc.perform(put("/transactions/41")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"amount": 0.0, "type": "cars"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAllowSameIdToBeUsedMultipleTimes() throws Exception {
        mockMvc.perform(put("/transactions/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"amount": 1000.0, "type": "cars"}
                            """))
                .andExpect(status().isOk());

        mockMvc.perform(put("/transactions/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"amount": 9999.0, "type": "food"}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void shouldReturnEmptyListForUnknownType() throws Exception {
        mockMvc.perform(get("/transactions/types/unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnTransactionIdsByType() throws Exception {
        mockMvc.perform(put("/transactions/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"amount": 1000.0, "type": "cars"}
                            """));
        mockMvc.perform(put("/transactions/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"amount": 2000.0, "type": "cars"}
                            """));
        mockMvc.perform(put("/transactions/102")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"amount": 3000.0, "type": "food"}
                            """));

        mockMvc.perform(get("/transactions/types/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", containsInAnyOrder(100, 101)));
    }

    @Test
    void shouldNotMixTransactionsFromDifferentTypes() throws Exception {
        mockMvc.perform(put("/transactions/200")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"amount": 1000.0, "type": "cars"}
                            """));
        mockMvc.perform(put("/transactions/201")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"amount": 2000.0, "type": "food"}
                            """));

        mockMvc.perform(get("/transactions/types/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(200));

        mockMvc.perform(get("/transactions/types/food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(201));
    }

    @Test
    void shouldUpdateTypeWhenTransactionIsReplaced() throws Exception {
        mockMvc.perform(put("/transactions/300")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"amount": 1000.0, "type": "cars"}
                            """));

        // Reemplazar con tipo diferente
        mockMvc.perform(put("/transactions/300")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {"amount": 1000.0, "type": "food"}
                            """));

        mockMvc.perform(get("/transactions/types/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(get("/transactions/types/food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsInAnyOrder(300)));
    }

    @Test
    void shouldNotDuplicateIdsWhenSameTransactionIsUpdatedMultipleTimes() throws Exception {
        mockMvc.perform(put("/transactions/400")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"amount": 1000.0, "type": "cars"}
                        """));

        mockMvc.perform(put("/transactions/400")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"amount": 2000.0, "type": "cars"}
                        """));

        mockMvc.perform(get("/transactions/types/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(400));
    }
}
