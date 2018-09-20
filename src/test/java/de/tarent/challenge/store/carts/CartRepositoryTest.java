package de.tarent.challenge.store.carts;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CartRepositoryTest {

    private static final String SKU1 = "123", SKU2 = "456";
    private static final int QUANTITY1 = 42, QUANTITY2 = 10;
    private static final BigDecimal PRICE = new BigDecimal(12.99);

    @Autowired
    private CartRepository cartRepository;

    @Before
    public void setUp() {
        cartRepository.deleteAll();
        Cart testCart = new Cart(Lists.newArrayList(
                new CartItem(SKU1, QUANTITY1, PRICE),
                new CartItem(SKU2, QUANTITY2, PRICE)));
        Cart testCart2 = new Cart(Lists.newArrayList(new CartItem(SKU2, QUANTITY2, PRICE)));
        cartRepository.saveAndFlush(testCart);
        cartRepository.saveAndFlush(testCart2);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void insert() {
        final List<Cart> all = cartRepository.findAll();
        assertEquals(2, all.size());
        assertEquals(SKU1, cartRepository.findById(all.get(0).getId()).get().getCartItems().get(0).getSku());
        assertEquals(SKU2, cartRepository.findById(all.get(1).getId()).get().getCartItems().get(0).getSku());

        Cart duplicate = new Cart(Lists.newArrayList(
                new CartItem(SKU1, 12345, PRICE),
                new CartItem(SKU1, 333, PRICE)));
        // should fail because of duplicate item sku
        cartRepository.saveAndFlush(duplicate);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void update() {
        final List<Cart> all = cartRepository.findAll();
        assertEquals(2, all.size());
        assertEquals(2, cartRepository.findById(all.get(0).getId()).get().getCartItems().size());
        assertEquals(SKU1, cartRepository.findById(all.get(0).getId()).get().getCartItems().get(0).getSku());
        assertEquals(SKU2, cartRepository.findById(all.get(0).getId()).get().getCartItems().get(1).getSku());
        assertEquals(1, cartRepository.findById(all.get(1).getId()).get().getCartItems().size());
        assertEquals(SKU2, cartRepository.findById(all.get(1).getId()).get().getCartItems().get(0).getSku());

        // add item to cart
        Cart cart1 = all.get(1);
        cart1.getCartItems().add(new CartItem(SKU2, 1, PRICE));
        cartRepository.saveAndFlush(cart1);
        assertEquals(2, cartRepository.findById(cart1.getId()).get().getCartItems().size());
        assertEquals(SKU2, cartRepository.findById(cart1.getId()).get().getCartItems().get(1).getSku());

        // remove items from cart
        cart1.getCartItems().removeIf(i -> i.getSku().equals(SKU2));
        cartRepository.saveAndFlush(cart1);
        assertEquals(1, cartRepository.findById(cart1.getId()).get().getCartItems().size());
        assertNull(cartRepository.findById(cart1.getId()).get().getCartItems().get(1).getSku());
        cart1.getCartItems().removeIf(i -> i.getSku().equals(SKU1));
        cartRepository.saveAndFlush(cart1);
        assertNull(cartRepository.findById(cart1.getId()));

        // update cart with duplicate sku
        Cart cart0 = all.get(0);
        cart0.getCartItems().add(new CartItem(SKU2, 1, PRICE));
        // should fail
        cartRepository.saveAndFlush(cart0);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void checkOut() {
        final List<Cart> all = cartRepository.findAll();
        assertEquals(2, all.size());
        assertEquals(2, cartRepository.findById(all.get(0).getId()).get().getCartItems().size());
        assertEquals(SKU1, cartRepository.findById(all.get(0).getId()).get().getCartItems().get(0).getSku());
        assertEquals(SKU2, cartRepository.findById(all.get(0).getId()).get().getCartItems().get(1).getSku());
        assertEquals(1, cartRepository.findById(all.get(1).getId()).get().getCartItems().size());
        assertEquals(SKU2, cartRepository.findById(all.get(1).getId()).get().getCartItems().get(0).getSku());

        Cart cart1 = all.get(1);
        cart1.setCheckedOut();
        cartRepository.saveAndFlush(cart1);

        // add item to cart
        cart1.getCartItems().add(new CartItem(SKU2, 1, PRICE));
        cartRepository.saveAndFlush(cart1);
    }
}