package de.tarent.challenge.store.carts;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
class Cart {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @NotEmpty
    @JoinColumn(name = "fk_cart")
    private List<CartItem> cartItems;

    @NotNull
    private boolean isCheckedOut = false;

    public Cart() {
    }

    public Cart(@NotEmpty List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getTotal() {
        return cartItems.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public void setCheckedOut() {
        isCheckedOut = true;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @PrePersist
    @PreUpdate
    @PreRemove
    public void checkLifeCycleState() {
        if (isCheckedOut)
            throw new UnsupportedOperationException("The cart is checked out and cannot be changed anymore.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart cart = (Cart) o;
        return isCheckedOut == cart.isCheckedOut &&
                Objects.equals(id, cart.id) &&
                Objects.equals(cartItems, cart.cartItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cartItems, isCheckedOut);
    }
}
