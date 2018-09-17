package de.tarent.challenge.store.products.dto;

import com.google.common.collect.Sets;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class ProductUpdateDto {

    @NotNull
    protected String name;

    @NotEmpty
    protected Set<String> eans;

    @NotNull
    protected BigDecimal price;

    public ProductUpdateDto(String name, Set<String> eans, BigDecimal price) {
        this.name = name;
        this.eans = eans;
        this.price = price;
    }

    public ProductUpdateDto() {
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
        return Objects.hash(name, eans, price);
    }
}
