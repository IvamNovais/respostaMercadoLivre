package com.resposta.resposta.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resposta.resposta.dto.FaturamentoDTO;
import com.resposta.resposta.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MercadoLivreService {

    private final RestTemplate restTemplate;
    @Value("${front.url}")
    private String FRONT_URL;
    @Value("${mercadoLivre.baseUrl}")
    private String BASE_URL;
    @Value("${mercadoLivre.clientId}")
    private String CLIENT_ID;
    @Value("${mercadoLivre.clientSecret}")
    private String CLIENT_SECRET;

    public MercadoLivreService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PerguntasResponse buscarPerguntasPorVendedor(String sellerId, String token) {
        // Adiciona o parâmetro status=UNANSWERED à URL
        String url = String.format("%squestions/search?seller_id=%s&status=UNANSWERED", BASE_URL, sellerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<PerguntasResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, PerguntasResponse.class);

        return response.getBody();
    }

    public String buscarProdutosPorVendedor(String sellerId, String token) {
        String url = String.format("%susers/%s/items/search", BASE_URL, sellerId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);


        return buscarDetalhesProdutosPorVendedor(Objects.requireNonNull(restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody()).split("results\":\\[\"")[1].split("\"]")[0].split(","), token);
    }

    public String buscarDetalhesProdutosPorVendedor(String[] produtos, String token) {
        StringBuilder resposta = new StringBuilder();
        for (String produto : produtos) {
            String url = String.format("https://api.mercadolibre.com/items/%s", produto+"?include_attributes=all");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            resposta.append(response.getBody()).append("\n");
        }

        return resposta.toString();
    }

    public String obterToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("code", authorizationCode);
        requestBody.add("redirect_uri", FRONT_URL);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        // Crie a entidade da solicitação com MultiValueMap
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Envie a solicitação POST
        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "oauth/token",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Verifique o código de status da resposta
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Erro ao obter token: " + response.getStatusCode());
        }
    }

    public String responderPergunta(String perguntaId, String resposta, String accessToken) {
        String url = BASE_URL + "answers";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.set("Content-Type", "application/json");

        // Criação do corpo da requisição
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("question_id", perguntaId);
        requestBody.put("text", resposta.replace("\"", ""));
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


        // Realiza a requisição POST
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return responseEntity.getBody();
    }

    public String buscarOrdens(String sellerId, String accessToken) {
        String url = BASE_URL + "orders/search?seller=" + sellerId;
        RestTemplate restTemplate = new RestTemplate();

        // Configurando os headers para a requisição
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Fazendo a requisição para a API do Mercado Livre
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Retornando a resposta da API como String
        return response.getBody();
    }

    public String buscarPedidos(String sellerId, String itemId, String accessToken) {
        String url = String.format("%sorders/search?seller=%s", BASE_URL, sellerId, itemId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public VisitData getVisits(String productId, String accessToken) {
        int month = 10;
        int year = 2024;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);

        // Criar a data de término para o último dia do mês especificado
        LocalDate endingDate = LocalDate.of(year, month, 1).withDayOfMonth(
                LocalDate.of(year, month, 1).lengthOfMonth()
        );

        // Formatar a data para o padrão yyyy-MM-dd
        String formattedEndingDate = endingDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        String url = String.format("%sitems/%s/visits/time_window?last=30&unit=day&ending=%s", BASE_URL, productId, formattedEndingDate);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, VisitData.class).getBody();
    }

    public FaturamentoDTO calcularFaturamento(String sellerId, String itemId, String accessToken) {
        try {
            String url = String.format("%sorders/search?seller=%s", BASE_URL, sellerId, itemId);
            RestTemplate restTemplate = new RestTemplate();

            // Configurando os headers para a requisição
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Fazendo a requisição para a API do Mercado Livre
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);


            // Usando ObjectMapper para ler a resposta JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(response.getBody());

            // Verificar se existe o campo 'results' no JSON
            if (!jsonResponse.has("results")) {
                throw new RuntimeException("Campo 'results' não encontrado na resposta da API");
            }

            JsonNode orders = jsonResponse.get("results");
            double totalFaturamento = 0.0;
            int quantidadeVendas = orders.size();
            double saleFee = 0.0;
            double saleValue =0.0;
            AtomicInteger unidadesVendidas = new AtomicInteger();
            for (JsonNode order : orders) {
                saleValue = order.get("total_amount").asDouble();
                saleFee = 0.0;

                // Acessa os itens do pedido para obter o valor da taxa de venda (sale_fee)
                if (order.has("order_items")) {
                    JsonNode orderItems = order.get("order_items");
                    for (JsonNode item : orderItems) {
                        unidadesVendidas.addAndGet(item.get("quantity").asInt());
                        saleFee += item.get("sale_fee").asDouble();
                    }
                }

                totalFaturamento +=new BigDecimal(saleValue - saleFee).setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
            double mediaFaturamento = quantidadeVendas > 0 ? totalFaturamento / quantidadeVendas : 0.0;

            return new FaturamentoDTO(totalFaturamento, mediaFaturamento, saleFee,quantidadeVendas, saleValue, unidadesVendidas.get());

        } catch (Exception e) {
            System.err.println("Erro ao calcular o faturamento: " + e.getMessage());
            return new FaturamentoDTO(0.0, 0.0, 0.0,0, 0.0,0);
        }
    }
    public Relatorio relatorio(String sellerId, String itemId, String accessToken){
        FaturamentoDTO faturamentoDTO = calcularFaturamento(sellerId,itemId,accessToken);
        VisitData visitData = getVisits(itemId, accessToken);
        double convercao = (double) faturamentoDTO.getQuantidadeVendas()/( visitData.getTotalVisits())*100 ;
        return Relatorio.builder().
                visitas(visitData.getTotalVisits())
                .qtdVendas(faturamentoDTO.getQuantidadeVendas())
                .conversao(new BigDecimal(convercao).setScale(2, RoundingMode.HALF_UP).doubleValue())
                .vendasBrutas(faturamentoDTO.getRendaBruta())
                .receita(faturamentoDTO.getTotalFaturamento())
                .unidadesVendidas(faturamentoDTO.getUnidadesVendidas())
                .build();
    }
    public String getUserInfo(String accessToken) {
        String url = "https://api.mercadolibre.com/users/me";

        // Configurando os headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        // Criando a entidade de requisição com os headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Fazendo a requisição GET
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Retornando o corpo da resposta
        return response.getBody();
    }
    public List<Question> buscarPerguntasPorVendedor(String sellerId, String asker, String token) {
        // Adiciona o parâmetro status=UNANSWERED à URL
        String url = String.format("%squestions/search?seller_id=%s", BASE_URL, sellerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);
        List<Question> questions = new ArrayList<>();

        ResponseEntity<PerguntasResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, PerguntasResponse.class);
        response.getBody().getQuestions().forEach(question -> {
            if(Long.toString(question.getFrom().getId()).equals(asker)){
                questions.add(question);
            }
        });
        return questions;
    }
    public String updateSkuInVariations(String itemId, String accessToken) {
        // URL para obter os detalhes do item
        String url = "https://api.mercadolibre.com/items/" + itemId + "?include_attributes=all";

        // Definir o cabeçalho com o token de acesso
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Fazer a requisição GET para obter os detalhes do item
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Produto> response = restTemplate.exchange(url, HttpMethod.GET, entity, Produto.class);

        // Verificar se a resposta foi bem-sucedida
        if (response.getStatusCode() == HttpStatus.OK) {
            Produto produto = response.getBody();
            if (produto != null) {
                // Criar o JSON contendo apenas a parte de SKU nas variações
                Map<String, Object> partialUpdate = generateSkuUpdatePayload(produto);

                // Fazer a requisição PUT para atualizar o SKU
                HttpEntity<Map<String, Object>> updateEntity = new HttpEntity<>(partialUpdate, headers);
                String updateUrl = "https://api.mercadolibre.com/items/" + itemId;
                ResponseEntity<String> updateResponse = restTemplate.exchange(updateUrl, HttpMethod.PUT, updateEntity, String.class);

                if (updateResponse.getStatusCode() == HttpStatus.OK) {
                    return "SKU atualizado com sucesso!";
                } else {
                    return "Erro ao atualizar SKU nas variações.";
                }
            } else {
                return "Erro: Produto não encontrado na resposta.";
            }
        } else {
            return "Erro ao obter informações do item.";
        }
    }

    private Map<String, Object> generateSkuUpdatePayload(Produto produto) {
        // Estrutura para atualizar apenas os SKUs
        List<Map<String, Object>> updatedVariations = new ArrayList<>();

        if (produto.getVariations() != null) {
            for (Produto.Variation variacao : produto.getVariations()) {
                // Cria um mapa para representar a variação e o novo SKU
                Map<String, Object> variationMap = new HashMap<>();
                variationMap.put("id", variacao.getId());
                List<Map<String, Object>> attributes = new ArrayList<>();
                Map<String, Object> skuAttribute = new HashMap<>();
                skuAttribute.put("id", "SELLER_SKU");
                skuAttribute.put("name", "SKU");
                String sku = gerarSku(produto.getTitle(), variacao.getAttributeCombinations().stream()
                        .map(att -> att.getValueName())
                        .collect(Collectors.joining())
                );

                skuAttribute.put("value_name", sku);

                List<Map<String, Object>> values = new ArrayList<>();
                Map<String, Object> value = new HashMap<>();
                value.put("id", null);
                value.put("name", sku);
                values.add(value);

                attributes.add(skuAttribute);
                variationMap.put("attributes", attributes);

                // Adiciona a variação ao payload de atualização
                updatedVariations.add(variationMap);
            }
        }

        // Mapa final contendo apenas as variações para atualizar o SKU
        Map<String, Object> payload = new HashMap<>();
        payload.put("variations", updatedVariations);
        return payload;
    }
    public static String abreviar(String texto) {
        return Arrays.stream(texto.split(" "))
                .filter(palavra -> palavra.length() > 2)
                .limit(3)
                .map(palavra -> palavra.substring(0, Math.min(3, palavra.length())).toUpperCase())
                .collect(Collectors.joining(""));
    }

    // Função para gerar o SKU
    public static String gerarSku(String nome, String atributo) {
        String abreviacaoNome = abreviar(nome);
        String abreviacaoAtributo = abreviar(atributo);
        return String.format("%s-%s", abreviacaoNome, abreviacaoAtributo);
    }

}
