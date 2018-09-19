package de.tarent.challenge.store.carts;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"FK_CART", "SKU"}))
class CartItem {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    private String sku;

    @NotNull
    private BigDecimal price;

    @NotNull
    private int quantity;

    public CartItem(@NotNull String sku, int quantity, @NotNull BigDecimal price) {
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItem() {
    }

    public BigDecimal getSubTotal() {
        return price.multiply(new BigDecimal(quantity));
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity &&
                //Objects.equals(id, cartItem.id) &&
                Objects.equals(sku, cartItem.sku) &&
                Objects.equals(price, cartItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(/*id, */sku, price, quantity);
    }
}
