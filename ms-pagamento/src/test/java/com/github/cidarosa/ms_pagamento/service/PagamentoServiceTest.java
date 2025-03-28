package com.github.cidarosa.ms_pagamento.service;

import com.github.cidarosa.ms_pagamento.repository.PagamentoRepository;
import com.github.cidarosa.ms_pagamento.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;

    // Não vamos acessar o DB
// preparando os dados
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;

        // precisa simular o comportamento do objeto mockado
        // delete - quando id existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);

        // delete - quando id não existe
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        // delete - primeiro caso - deleta
        // não faça nada (void) quando ...
        Mockito.doNothing().when(repository).deleteById(existingId);
    }

    @Test
    @DisplayName("delete Deveria não fazer nada quando Id existe")
    public void deleteShouldDoNothingWhenIdExists() {
        // em PagamentoService, o método delete é do tipo void
        Assertions.assertDoesNotThrow(
                () -> {
                    service.deletePagamento(existingId);
                }
        );
    }

    @Test
    @DisplayName("delete Deveria lançar exceção ResourceNotFoundException quando Id não existe")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.deletePagamento(nonExistingId);
                }
        );
    }


}
