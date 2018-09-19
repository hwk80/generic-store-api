package de.tarent.challenge.store.carts.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

public class CartItemCreateDto {

    @NotNull
    String sku;
    @NotNull
    @PositiveOrZero
    int quantity;

    public CartItemCreateDto() {
    }

    public CartItemCreateDto(@NotNull String sku, @NotNull @PositiveOrZero int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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
        if (!(o instanceof CartItemCreateDto)) return false;
        CartItemCreateDto that = (CartItemCreateDto) o;
        return quantity == that.quantity &&
                Objects.equals(sku, that.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, quantity);
    }
}
