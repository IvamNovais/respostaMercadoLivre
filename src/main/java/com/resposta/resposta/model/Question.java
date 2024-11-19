package com.resposta.resposta.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Question {
    private String date_created;
    private String item_id;
    private long seller_id;
    private String status;
    private String text;
    private List<String> tags;
    private long id;
    private boolean deleted_from_listing;
    private boolean hold;
    private Answer answer;
    private From from;
}
