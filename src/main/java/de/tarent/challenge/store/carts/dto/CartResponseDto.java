package de.tarent.challenge.store.carts.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class CartResponseDto {

    private Long id;

    private List<CartItemResponseDto> cartItems;

    private BigDecimal total;

    public CartResponseDto() {
    }

    public CartResponseDto(List<CartItemResponseDto> cartItems, BigDecimal total) {
        this.cartItems = cartItems;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemResponseDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemResponseDto> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartResponseDto)) return false;
        CartResponseDto that = (CartResponseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(cartItems, that.cartItems) &&
                Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cartItems, total);
    }
}
