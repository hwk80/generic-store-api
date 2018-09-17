package de.tarent.challenge.store.products.dto;

import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

public class ProductUpdateDto {

    @NotNull
    protected String name;

    @NotEmpty
    protected Set<String> eans;

    public ProductUpdateDto(String name, Set<String> eans) {
        this.name = name;
        this.eans = eans;
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

    @Override
    public int hashCode() {
        return Objects.hash(name, eans);
    }
}
