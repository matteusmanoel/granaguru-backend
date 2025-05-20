package app.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.repositories.TagRepository;

public class TagServiceTest {
    @Mock
    private TagRepository repo;

    @InjectMocks
    private TagService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Cenário base para TagService")
    void testMetodoExemplo() {
        assertTrue(true);
    }
}
