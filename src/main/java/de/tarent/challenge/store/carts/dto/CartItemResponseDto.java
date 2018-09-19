package de.tarent.challenge.store.carts.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class CartItemResponseDto {

    String sku;
    String name;
    int quantity;
    BigDecimal price;
    BigDecimal subTotal;

    public CartItemResponseDto() {
    }

    public CartItemResponseDto(String sku, String name, int quantity, BigDecimal price, BigDecimal subTotal) {
        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItemResponseDto)) return false;
        CartItemResponseDto that = (CartItemResponseDto) o;
        return quantity == that.quantity &&
                Objects.equals(sku, that.sku) &&
                Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) &&
                Objects.equals(subTotal, that.subTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, name, quantity, price, subTotal);
    }
}
