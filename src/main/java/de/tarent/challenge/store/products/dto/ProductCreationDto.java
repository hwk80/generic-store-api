package de.tarent.challenge.store.products.dto;

import com.google.common.collect.Sets;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class ProductCreationDto {

    @NotNull
    private String sku;
    @NotNull
    private String name;
    @NotEmpty
    private Set<String> eans;
    @NotNull
    private BigDecimal price;

    private boolean isAvailable = true;

    public ProductCreationDto(String sku, String name, Set<String> eans, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.eans = eans;
        this.price = price;
    }

    public ProductCreationDto() {
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
        if (!(o instanceof ProductCreationDto)) return false;
        ProductCreationDto that = (ProductCreationDto) o;
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
