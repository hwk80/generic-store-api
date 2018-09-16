package de.tarent.challenge.store.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductUpdateDto extends ProductCreationDto {

    @JsonIgnore
    protected String sku;
}
