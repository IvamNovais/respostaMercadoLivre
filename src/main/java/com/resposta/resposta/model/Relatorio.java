package com.resposta.resposta.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Relatorio {
    private double vendasBrutas;
    private int visitas;
    private int qtdVendas;
    private double conversao;
    private int vendasComPublicidade;
    private int vendasSemPublicidade;
    private double impressoes;
    private double receita;
    private int unidadesVendidas;

}
