package de.tarent.challenge.store.carts.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

public class CartUpsertDto {

    @NotEmpty
    private List<CartItemCreateDto> cartItems;

    public List<CartItemCreateDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemCreateDto> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartUpsertDto)) return false;
        CartUpsertDto that = (CartUpsertDto) o;
        return Objects.equals(cartItems, that.cartItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItems);
    }
}
