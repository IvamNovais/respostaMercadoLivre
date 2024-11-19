package com.resposta.resposta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FaturamentoDTO {
    private double totalFaturamento;
    private double mediaFaturamento;
    private double totalTaxa;
    private int quantidadeVendas;
    private double rendaBruta;
    private int unidadesVendidas;
}