package com.github.cidarosa.ms_pagamento.tests;

import com.github.cidarosa.ms_pagamento.entity.Pagamento;
import com.github.cidarosa.ms_pagamento.entity.Status;

import java.math.BigDecimal;

public class Factory {

    public static Pagamento createPagamento() {
        Pagamento pagamento = new Pagamento(
                1L,
                BigDecimal.valueOf(32.25),
                "Jon Snow",
                "2365412478964521",
                "07/32",
                "585",
                Status.CRIADO,
                1L,
                2L
        );

        return pagamento;
    }

}
