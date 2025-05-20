package app.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.repositories.OrcamentoRepository;

public class OrcamentoServiceTest {
    @Mock
    private OrcamentoRepository repo;

    @InjectMocks
    private OrcamentoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Cenário base para OrcamentoService")
    void testMetodoExemplo() {
        assertTrue(true);
    }
}
