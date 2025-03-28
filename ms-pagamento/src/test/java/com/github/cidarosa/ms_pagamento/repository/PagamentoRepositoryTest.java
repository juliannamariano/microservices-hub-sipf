package com.github.cidarosa.ms_pagamento.repository;

import com.github.cidarosa.ms_pagamento.entity.Pagamento;
import com.github.cidarosa.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTest {


    @Autowired
    private PagamentoRepository repository;


    // declarando as variáveis
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalPagamento;

    // Vai ser executado ANTES de cada teste
    @BeforeEach
    void setup() throws Exception {
        // Arrange
        existingId = 1L;
        nonExistingId = 100L;
        countTotalPagamento = 3L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        // Act
        repository.deleteById(existingId);

        // Assert
        // Após deletar, tenta encontrar o objeto com o ID no repositório.
        Optional<Pagamento> result = repository.findById(existingId);

        // testa se existe um obj dentro do Optional
        // verifica se o objeto não está presente no repositório.
        // Se result.isPresent() for false,
        // significa que o objeto foi deletado com sucesso.
        Assertions.assertFalse(result.isPresent());
    }

    @Test
// Fornece uma descrição legível do teste, que será exibida nos relatórios de teste
    @DisplayName("Dado parâmetros válidos e Id nulo " +
            "quando chamar Criar Pagamento " +
            "então deve instanciar um Pagamento")
    public void givenValidParamsAndIdIsNull_whenCallCreatePagamento_thenInstantiateAPagamento() {

        Pagamento pagamento = Factory.createPagamento();
        pagamento.setId(null);
        pagamento = repository.save(pagamento);

        Assertions.assertNotNull(pagamento.getId());
        // verifica se o ID gerado é o próximo
        Assertions.assertEquals(countTotalPagamento + 1, pagamento.getId());
    }


}
