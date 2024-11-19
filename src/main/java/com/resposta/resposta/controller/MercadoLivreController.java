package com.resposta.resposta.controller;

import com.resposta.resposta.dto.FaturamentoDTO;
import com.resposta.resposta.model.PerguntasResponse;
import com.resposta.resposta.model.Question;
import com.resposta.resposta.model.Relatorio;
import com.resposta.resposta.model.VisitData;
import com.resposta.resposta.service.MercadoLivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MercadoLivreController {

    @Autowired
    private MercadoLivreService mercadoLivreService;
    @GetMapping("/mercadolivre/questions/{id}")
    public PerguntasResponse getQuestions(@PathVariable String id, @RequestHeader("Authorization") String authorizationHeader) {
        return mercadoLivreService.buscarPerguntasPorVendedor(id,authorizationHeader);
    }
    @GetMapping("/mercadolivre/questions/{sallerId}/{askerId}")
    public List<Question> getQuestions(@PathVariable String sallerId, @PathVariable String askerId, @RequestHeader("Authorization") String authorizationHeader) {
        return mercadoLivreService.buscarPerguntasPorVendedor(sallerId,askerId,authorizationHeader);
    }
    @GetMapping("/mercadolivre/products/{id}")
    public String getProducts(@PathVariable String id, @RequestHeader("Authorization") String authorizationHeader) {
        return mercadoLivreService.buscarProdutosPorVendedor(id,authorizationHeader);
    }

    @PostMapping("/token/{code}")
    public ResponseEntity<String> obterToken(@PathVariable("code") String authorizationCode) {
        try {
            String tokenResponse = mercadoLivreService.obterToken(authorizationCode);
            return ResponseEntity.ok(tokenResponse); // Retorna o token como resposta
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao obter o token: " + e.getMessage());
        }
    }
    @PostMapping("/responder-pergunta/{perguntaId}")
    public String responderPergunta(
            @PathVariable String perguntaId,
            @RequestBody String resposta,
            @RequestHeader("Authorization") String authorizationCode) {
        return mercadoLivreService.responderPergunta(perguntaId, resposta, authorizationCode);
    }
    @GetMapping("/detalhes/{sellerId}/{produto}")
    public ResponseEntity<String> buscarDetalhesProdutosPorVendedor(
            @PathVariable String sellerId,
            @PathVariable String produto,
            @RequestHeader("Authorization") String token) {

        String detalhesProdutos;
        String[] produtos = new String[1];
        produtos[0]=produto;
        try {
            detalhesProdutos = mercadoLivreService.buscarDetalhesProdutosPorVendedor(produtos, token);
            return ResponseEntity.ok(detalhesProdutos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar detalhes dos produtos: " + e.getMessage());
        }
    }
    @GetMapping("/ordens/{sellerId}")
    public ResponseEntity<String> buscarOrdens(
            @PathVariable("sellerId") String sellerId,
            @RequestHeader("Authorization") String accessToken) {
        // O accessToken já virá no formato "Bearer SEU_TOKEN"
        String resultado = mercadoLivreService.buscarOrdens(sellerId, accessToken.replace("Bearer ", ""));
        return ResponseEntity.ok(resultado);
    }
    @GetMapping("/buscar-pedidos/{sellerId}/{itemId}")
    public ResponseEntity<String> buscarPedidos(
            @PathVariable("sellerId") String sellerId,
            @PathVariable("itemId") String itemId,
            @RequestHeader("Authorization") String accessToken) {
        String pedidos = mercadoLivreService.buscarPedidos(sellerId, itemId, accessToken);
        return ResponseEntity.ok(pedidos);
    }
    @GetMapping("/mercadolivre/products/visitas/{id}")
    public VisitData getProductsVisitas(@PathVariable String id, @RequestHeader("Authorization") String authorizationHeader) {
        return mercadoLivreService.getVisits(id,authorizationHeader);
    }
    @GetMapping("/faturamento/{sellerId}/{itemId}")
    public ResponseEntity<FaturamentoDTO> calculaMetricas(
            @PathVariable("sellerId") String sellerId,
            @PathVariable("itemId") String itemId,
            @RequestHeader("Authorization") String accessToken) {
        FaturamentoDTO faturamento = mercadoLivreService.calcularFaturamento(sellerId, itemId, accessToken);
        return ResponseEntity.ok(faturamento);
    }
    @GetMapping("/relatorio/{sellerId}/{itemId}")
    public ResponseEntity<Relatorio> relatorios(
            @PathVariable("sellerId") String sellerId,
            @PathVariable("itemId") String itemId,
            @RequestHeader("Authorization") String accessToken) {
        Relatorio faturamento = mercadoLivreService.relatorio(sellerId, itemId, accessToken);
        return ResponseEntity.ok(faturamento);
    }
    @GetMapping("/user-info")
    public ResponseEntity<String> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // Extrai o token do header Authorization
        String accessToken = authorizationHeader.replace("Bearer ", "");

        // Chama o serviço para obter as informações do usuário
        String userInfo = mercadoLivreService.getUserInfo(accessToken);

        // Retorna a resposta como um ResponseEntity
        return ResponseEntity.ok(userInfo);
    }
        @PutMapping("/update-sku/{itemId}")
    public ResponseEntity<String> updateSku(@PathVariable String itemId, @RequestHeader("Authorization") String token) {
        // Chama o serviço para atualizar o SKU nas variações do produto
        String result = mercadoLivreService.updateSkuInVariations(itemId, token);

        if (result.equals("SKU atualizado com sucesso!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
