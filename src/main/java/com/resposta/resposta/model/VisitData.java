package com.resposta.resposta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

public class VisitData {

    @JsonProperty("item_id")
    private String itemId;

    @JsonProperty("date_from")
    private ZonedDateTime dateFrom;

    @JsonProperty("date_to")
    private ZonedDateTime dateTo;

    @JsonProperty("total_visits")
    private int totalVisits;

    @JsonProperty("last")
    private int last;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("results")
    private List<Result> results;

    public static class Result {

        @JsonProperty("date")
        private ZonedDateTime date;

        @JsonProperty("total")
        private int total;

        @JsonProperty("visits_detail")
        private List<VisitDetail> visitsDetail;

        public static class VisitDetail {

            @JsonProperty("company")
            private String company;

            @JsonProperty("quantity")
            private int quantity;

            // Getters e Setters para VisitDetail
            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
        }

        // Getters e Setters para Result
        public ZonedDateTime getDate() {
            return date;
        }

        public void setDate(ZonedDateTime date) {
            this.date = date;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<VisitDetail> getVisitsDetail() {
            return visitsDetail;
        }

        public void setVisitsDetail(List<VisitDetail> visitsDetail) {
            this.visitsDetail = visitsDetail;
        }
    }

    // Getters e Setters para VisitData
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(ZonedDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(int totalVisits) {
        this.totalVisits = totalVisits;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
