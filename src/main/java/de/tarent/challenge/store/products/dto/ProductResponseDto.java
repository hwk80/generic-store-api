package de.tarent.challenge.store.products.dto;

import com.google.common.collect.Sets;

import java.util.Set;

public class ProductResponseDto {

    protected String sku;
    protected String name;
    protected Set<String> eans;

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
}
