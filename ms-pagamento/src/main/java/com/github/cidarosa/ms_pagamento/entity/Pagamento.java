package com.github.cidarosa.ms_pagamento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal valor;
    private String nome;              // nome no cartão
    private String numeroDoCartao;    // xxxx xxxx xxxx xxxx
    private String validade;          // MM/AA
    private String codigoDeSeguranca; // xxx
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    private Long pedidoId;
    @Column(nullable = false)
    private Long formaDePagamentoId;  // 1 - dinheiro | 2 - cartão | 3 - pix

}
