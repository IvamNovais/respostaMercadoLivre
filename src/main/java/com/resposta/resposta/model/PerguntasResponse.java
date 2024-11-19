package com.resposta.resposta.model;

import lombok.Data;
import java.util.List;

@Data
public class PerguntasResponse {
    private int total;
    private int limit;
    private List<Question> questions;
    private List<String> available_sorts;
}
