package com.github.cidarosa.ms_pagamento.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cidarosa.ms_pagamento.dto.PagamentoDTO;
import com.github.cidarosa.ms_pagamento.service.PagamentoService;
import com.github.cidarosa.ms_pagamento.service.exceptions.ResourceNotFoundException;
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

        // simulando o comportamento do service - getById
        // Id existe
        Mockito.when(service.getById(existingId)).thenReturn(dto);
        // Id não existe - lança exception
        Mockito.when(service.getById(nonExistsId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.createPagamento(any())).thenReturn(dto);

        Mockito.when(service.updatePagamento(eq(existingId), any())).thenReturn(dto);
        Mockito.when(service.updatePagamento(eq(nonExistsId), any()))
                .thenThrow(ResourceNotFoundException.class);

        // simulando o comportamento do service - deletePagamento
        Mockito.doNothing().when(service).deletePagamento(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).deletePagamento(nonExistsId);


    }

    @Test
    public void getAllShouldReturnListPagamentoDTO() throws Exception {

        // chamando requisição com o método GET em /pagamentos
        ResultActions result = mockMvc.perform(get("/pagamentos").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void getByIdShouldReturnPagamentoDTOWhenIdExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        // Assertions
        result.andExpect(status().isOk());
        // verifica se tem os campos em result
        // $ - acessar o objeto result
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void getByIdShouldReturnNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", nonExistsId)
                .accept(MediaType.APPLICATION_JSON));

        // Assertions
        result.andExpect(status().isNotFound());
    }

    @Test
    public void createPagamentoShouldReturnPagamentoDTOCreated() throws Exception {
        PagamentoDTO newPagamentoDTO = Factory.createNewPagamentoDTO();

        // Bean objectMapper usado para converter JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(newPagamentoDTO);

        // POST - tem corpo na requisição - JSON
        mockMvc.perform(post("/pagamentos")
                        .content(jsonRequestBody) // RequestBody
                        .contentType(MediaType.APPLICATION_JSON) // request
                        .accept(MediaType.APPLICATION_JSON))     // response
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.pedidoId").exists())
                .andExpect(jsonPath("$.formaDePagamentoId").exists());
    }

    @Test
    public void updatePagamentoShouldReturnPagamentoDTOWhenIdExist() throws Exception {
        // Bean objectMapper usado para converter JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(dto);

        // PUT - tem corpo na requisição - JSON
        // é preciso passar o corpo da requisição
        mockMvc.perform(put("/pagamentos/{id}", existingId)
                        .content(jsonRequestBody) // RequestBody
                        .contentType(MediaType.APPLICATION_JSON) // request
                        .accept(MediaType.APPLICATION_JSON))     // response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.pedidoId").exists())
                .andExpect(jsonPath("$.formaDePagamentoId").exists());
    }

    @Test
    public void updatePagamentoShouldReturnNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        // Bean objectMapper usado para converter JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(dto);

        // PUT - tem corpo na requisição - JSON
        // é preciso passar o corpo da requisição
        mockMvc.perform(put("/pagamentos/{id}", nonExistsId)
                        .content(jsonRequestBody) // RequestBody
                        .contentType(MediaType.APPLICATION_JSON) // request
                        .accept(MediaType.APPLICATION_JSON))     // response
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePagamentoShouldDoNothingWhenIdExist() throws Exception {
        mockMvc.perform(delete("/pagamentos/{id}", nonExistsId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePagamentoShouldReturnNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/pagamentos/{id}", nonExistsId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}
