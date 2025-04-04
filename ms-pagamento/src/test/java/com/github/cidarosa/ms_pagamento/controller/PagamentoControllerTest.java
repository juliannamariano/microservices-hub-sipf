package com.github.cidarosa.ms_pagamento.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cidarosa.ms_pagamento.dto.PagamentoDTO;
import com.github.cidarosa.ms_pagamento.service.PagamentoService;
import com.github.cidarosa.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import java.util.List;

@WebMvcTest
public class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc; // para chamar o endpoint
    // controller tem dependência do service
    // dependência mockada

    @MockitoBean
    private PagamentoService service;
    private PagamentoDTO dto;
    private Long existingId;
    private Long nonExistsId;

    // converter para JSON o objeto Java e enviar na requisição
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {

        existingId = 1L;
        nonExistsId = 100L;

        // criando um PagamentoDTO
        dto = Factory.CREATEpAGAMENTOdto();
        // Listando PagamentoDTO
        List<PagamentoDTO> list = List.of(dto);

        // simulando o comportamento do service - getAll
        Mockito.when(service.getAll()).thenReturn(list);
    }

    @Test
    public void getAllShouldReturnListPagamentoDTO() throws Exception {

        // chamando requisição com o método GET em /pagamentos
        ResultActions result = mockMvc.perform(get("/pagamentos").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }


}
