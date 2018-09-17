package de.tarent.challenge.store.products.dto;

import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class ProductResponseDto {

    protected String sku;
    protected String name;
    protected Set<String> eans;
    protected BigDecimal price;

    public ProductResponseDto() {
    }

    public ProductResponseDto(String sku, String name, Set<String> eans, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.eans = eans;
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getEans() {
        return Sets.newHashSet(eans);
    }

    public void setEans(Set<String> eans) {
        this.eans = eans;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, name, eans, price);
    }
}
