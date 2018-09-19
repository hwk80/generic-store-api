package de.tarent.challenge.store.products.dto;

import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class ProductResponseDto {

    private String sku;
    private String name;
    private Set<String> eans;
    private BigDecimal price;
    private boolean isAvailable;

    public ProductResponseDto() {
    }

    public ProductResponseDto(String sku, String name, Set<String> eans, BigDecimal price, boolean isAvailable) {
        this.sku = sku;
        this.name = name;
        this.eans = eans;
        this.price = price;
        this.isAvailable = isAvailable;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductResponseDto)) return false;
        ProductResponseDto that = (ProductResponseDto) o;
        return isAvailable == that.isAvailable &&
                Objects.equals(sku, that.sku) &&
                Objects.equals(name, that.name) &&
                Objects.equals(eans, that.eans) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, name, eans, price, isAvailable);
    }
}
