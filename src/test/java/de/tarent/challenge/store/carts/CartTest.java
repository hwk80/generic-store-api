package de.tarent.challenge.store.carts;

import com.google.common.collect.Lists;
import de.tarent.challenge.store.carts.dto.CartItemCreateDto;
import de.tarent.challenge.store.carts.dto.CartResponseDto;
import de.tarent.challenge.store.carts.dto.CartUpsertDto;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class CartTest {

    private static final String SKU = "123";
    private static final BigDecimal PRICE = new BigDecimal(12.99);

    private static final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void upsertDtoMapping() {
        CartUpsertDto createDto = new CartUpsertDto();
        createDto.setCartItems(Lists.newArrayList(new CartItemCreateDto(SKU, 12)));

        Cart cart = modelMapper.map(createDto, Cart.class);
        assertEquals(createDto.getCartItems().get(0).getSku(), cart.getCartItems().get(0).getSku());
        assertEquals(createDto.getCartItems().get(0).getQuantity(), cart.getCartItems().get(0).getQuantity());

        cart = new Cart(Lists.newArrayList(new CartItem(SKU, 555, PRICE)));
        createDto = modelMapper.map(cart, CartUpsertDto.class);
        assertEquals(cart.getCartItems().get(0).getSku(), createDto.getCartItems().get(0).getSku());
        assertEquals(cart.getCartItems().get(0).getQuantity(), createDto.getCartItems().get(0).getQuantity());
    }

    @Test
    public void responseDtoMapping() {
        Cart cart = new Cart(Lists.newArrayList(new CartItem(SKU, 555, PRICE)));
        CartResponseDto responseDto = modelMapper.map(cart, CartResponseDto.class);
        assertEquals(cart.getCartItems().get(0).getSku(), responseDto.getCartItems().get(0).getSku());
        assertEquals(cart.getCartItems().get(0).getQuantity(), responseDto.getCartItems().get(0).getQuantity());
        assertEquals(cart.getCartItems().get(0).getPrice(), responseDto.getCartItems().get(0).getPrice());
        assertEquals(cart.getCartItems().get(0).getSubTotal(), responseDto.getCartItems().get(0).getSubTotal());
        assertEquals(cart.getTotal(), responseDto.getTotal());
    }

}