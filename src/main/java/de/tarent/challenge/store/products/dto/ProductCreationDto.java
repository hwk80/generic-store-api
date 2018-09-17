package de.tarent.challenge.store.products.dto;

import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

public class ProductCreationDto {

    @NotNull
    protected String sku;

    @NotNull
    protected String name;

    @NotEmpty
    protected Set<String> eans;

    public ProductCreationDto(String sku, String name, Set<String> eans) {
        this.sku = sku;
        this.name = name;
        this.eans = eans;
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

    @Override
    public int hashCode() {
        return Objects.hash(sku, name, eans);
    }
}
