package de.tarent.challenge.store.products;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private String sku;

    @NotNull
    @Column(nullable = false)
    private String name;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @NotEmpty
    @Column(nullable = false)
    private Set<String> eans;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private Product() {
    }

    public Product(String sku, String name, Set<String> eans, BigDecimal price) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(sku, product.sku) &&
                Objects.equals(name, product.name) &&
                Objects.equals(eans, product.eans) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, name, eans, price);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("sku", sku)
                .add("name", name)
                .add("eans", eans)
                .add("price", price)
                .toString();
    }
}
