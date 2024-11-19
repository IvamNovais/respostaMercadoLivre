package com.resposta.resposta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Produto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("site_id")
    private String siteId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("seller_id")
    private Long sellerId;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("official_store_id")
    private Integer officialStoreId;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("base_price")
    private Double basePrice;

    @JsonProperty("original_price")
    private Double originalPrice;

    @JsonProperty("currency_id")
    private String currencyId;

    @JsonProperty("initial_quantity")
    private Integer initialQuantity;

    @JsonProperty("sale_terms")
    private List<SaleTerm> saleTerms;

    @JsonProperty("buying_mode")
    private String buyingMode;

    @JsonProperty("listing_type_id")
    private String listingTypeId;

    @JsonProperty("condition")
    private String condition;

    @JsonProperty("permalink")
    private String permalink;

    @JsonProperty("thumbnail_id")
    private String thumbnailId;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("pictures")
    private List<Picture> pictures;

    @JsonProperty("video_id")
    private String videoId;

    @JsonProperty("descriptions")
    private List<Object> descriptions;

    @JsonProperty("accepts_mercadopago")
    private Boolean acceptsMercadopago;

    @JsonProperty("non_mercado_pago_payment_methods")
    private List<Object> nonMercadoPagoPaymentMethods;

    @JsonProperty("shipping")
    private Shipping shipping;

    @JsonProperty("international_delivery_mode")
    private String internationalDeliveryMode;

    @JsonProperty("seller_address")
    private SellerAddress sellerAddress;

    @JsonProperty("seller_contact")
    private Object sellerContact;

    @JsonProperty("location")
    private Object location;

    @JsonProperty("coverage_areas")
    private List<Object> coverageAreas;

    @JsonProperty("attributes")
    private List<Attribute> attributes;

    @JsonProperty("listing_source")
    private String listingSource;

    @JsonProperty("variations")
    private List<Variation> variations;

    @JsonProperty("status")
    private String status;

    @JsonProperty("sub_status")
    private List<String> subStatus;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("warranty")
    private String warranty;

    @JsonProperty("catalog_product_id")
    private String catalogProductId;

    @JsonProperty("domain_id")
    private String domainId;

    @JsonProperty("parent_item_id")
    private String parentItemId;

    @JsonProperty("deal_ids")
    private List<Object> dealIds;

    @JsonProperty("automatic_relist")
    private Boolean automaticRelist;

    @JsonProperty("date_created")
    private String dateCreated;

    @JsonProperty("last_updated")
    private String lastUpdated;

    @JsonProperty("health")
    private Double health;

    @JsonProperty("catalog_listing")
    private Boolean catalogListing;

    // Getters and setters

    public static class SaleTerm {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("value_id")
        private String valueId;

        @JsonProperty("value_name")
        private String valueName;

        @JsonProperty("value_struct")
        private Object valueStruct;

        @JsonProperty("values")
        private List<Value> values;

        @JsonProperty("value_type")
        private String valueType;

        // Getters and setters
    }
    @Data
    public static class Value {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("struct")
        private Object struct;

        // Getters and setters
    }

    public static class Picture {
        @JsonProperty("id")
        private String id;

        @JsonProperty("url")
        private String url;

        @JsonProperty("secure_url")
        private String secureUrl;

        @JsonProperty("size")
        private String size;

        @JsonProperty("max_size")
        private String maxSize;

        @JsonProperty("quality")
        private String quality;

        // Getters and setters
    }

    public static class Shipping {
        @JsonProperty("mode")
        private String mode;

        @JsonProperty("methods")
        private List<Object> methods;

        @JsonProperty("tags")
        private List<String> tags;

        @JsonProperty("dimensions")
        private Object dimensions;

        @JsonProperty("local_pick_up")
        private Boolean localPickUp;

        @JsonProperty("free_shipping")
        private Boolean freeShipping;

        @JsonProperty("logistic_type")
        private String logisticType;

        @JsonProperty("store_pick_up")
        private Boolean storePickUp;

        // Getters and setters
    }

    public static class SellerAddress {
        @JsonProperty("city")
        private Location city;

        @JsonProperty("state")
        private Location state;

        @JsonProperty("country")
        private Location country;

        @JsonProperty("search_location")
        private SearchLocation searchLocation;

        @JsonProperty("id")
        private Long id;

        // Getters and setters
    }

    public static class Location {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        // Getters and setters
    }

    public static class SearchLocation {
        @JsonProperty("city")
        private Location city;

        @JsonProperty("state")
        private Location state;

        // Getters and setters
    }

    @Data
    public static class Attribute {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("value_id")
        private String valueId;

        @JsonProperty("value_name")
        private String valueName;

        @JsonProperty("values")
        private List<Value> values;

        @JsonProperty("value_type")
        private String valueType;

        public static Attribute createSkuAttribute(String skuValue) {
            Attribute sku = new Attribute();
            sku.setId("SKU");
            sku.setName("SKU");
            sku.setValueId(skuValue);
            sku.setValueName(skuValue);
            sku.setValues(new ArrayList<>());
            Value value = new Value();
            value.setName(skuValue);
            sku.getValues().add(value);
            sku.setValueType("string");
            return sku;
        }

        // Getters and setters
    }

    @Data
    public static class Variation {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("price")
        private Double price;

        @JsonProperty("attribute_combinations")
        private List<AttributeCombination> attributeCombinations;

        @JsonProperty("sale_terms")
        private List<Object> saleTerms;

        @JsonProperty("picture_ids")
        private List<String> pictureIds;

        @JsonProperty("catalog_product_id")
        private String catalogProductId;

        @JsonProperty("attributes")
        private List<Attribute> attributes;

        // Getters and setters
    }
    @Data
    public static class AttributeCombination {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("value_id")
        private String valueId;

        @JsonProperty("value_name")
        private String valueName;

        @JsonProperty("values")
        private List<Value> values;

        @JsonProperty("value_type")
        private String valueType;

        // Getters and setters
    }
}
