package de.tarent.challenge.store.carts;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.tarent.challenge.store.carts.dto.CartItemCreateDto;
import de.tarent.challenge.store.carts.dto.CartResponseDto;
import de.tarent.challenge.store.carts.dto.CartUpsertDto;
import de.tarent.challenge.store.exceptions.ProductNotAvailableException;
import de.tarent.challenge.store.products.ProductService;
import de.tarent.challenge.store.products.dto.ProductResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CartServiceTest {

    private static final String SKU1 = "123", SKU2 = "456",
            NAME1 = "Test Product", NAME2 = "Test Product 2";
    private static final Set<String> EANS = Sets.newHashSet("ean1", "ean2", "ean3");
    private static final int QUANTITY1 = 42, QUANTITY2 = 10;
    private static final BigDecimal PRICE = new BigDecimal(12.99);

    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private ProductService productService;

    private CartService cartService;

    @Before
    public void setUp() {
        cartService = new CartService(cartRepository, productService);

        Cart testCart = new Cart(Lists.newArrayList(
                new CartItem(SKU1, QUANTITY1, PRICE),
                new CartItem(SKU2, QUANTITY2, PRICE)));

        given(cartRepository.findById(anyLong())).willReturn(Optional.of(testCart));
        given(productService.retrieveProductBySku(SKU1)).willReturn(new ProductResponseDto(SKU1, NAME1, EANS, PRICE, true));
        given(productService.retrieveProductBySku(SKU2)).willReturn(new ProductResponseDto(SKU2, NAME2, EANS, PRICE, true));
    }

    @Test
    public void createCart() {
        final CartUpsertDto cartUpsertDto = new CartUpsertDto();
        cartUpsertDto.setCartItems(Lists.newArrayList(
                new CartItemCreateDto(SKU1, QUANTITY1),
                new CartItemCreateDto(SKU2, QUANTITY2)));

        cartService.createCart(cartUpsertDto);

        verify(cartRepository, times(1))
                .saveAndFlush(Mockito.any(Cart.class));
        verify(productService, times(2))
                .retrieveProductBySku(Mockito.anyString());
    }

    @Test(expected = ProductNotAvailableException.class)
    public void createCartWithUnavailableProduct() {
        given(productService.retrieveProductBySku("n/a")).willReturn(null);

        final CartUpsertDto cartUpsertDto = new CartUpsertDto();
        cartUpsertDto.setCartItems(Lists.newArrayList(
                new CartItemCreateDto(SKU1, QUANTITY1),
                new CartItemCreateDto("n/a", QUANTITY2)));

        cartService.createCart(cartUpsertDto);
    }

    @Test
    public void addToCart() {
        CartItemCreateDto createDto = new CartItemCreateDto(SKU1, QUANTITY1);
        cartService.addToCart(1L, createDto);

        verify(cartRepository, times(1))
                .findById(Mockito.eq(1L));
        verify(productService, times(1))
                .retrieveProductBySku(Mockito.eq(SKU1));
        verify(cartRepository, times(1))
                .saveAndFlush(Mockito.any(Cart.class));
    }

    @Test(expected = ProductNotAvailableException.class)
    public void addUnavailableProductToCart() {
        given(productService.retrieveProductBySku(SKU1))
                .willReturn(new ProductResponseDto(SKU1, NAME1, EANS, PRICE, false));

        CartItemCreateDto createDto = new CartItemCreateDto(SKU1, QUANTITY1);
        cartService.addToCart(1L, createDto);
    }

    @Test
    public void removeFromCart() {
        final CartResponseDto responseDto = cartService.removeFromCart(1L, SKU1);

        verify(cartRepository, times(1))
                .findById(Mockito.eq(1L));
        verify(cartRepository, never())
                .delete(Mockito.any());
        verify(cartRepository, times(1))
                .saveAndFlush(Mockito.any(Cart.class));

        assertEquals(1, responseDto.getCartItems().size());
        assertEquals(SKU2, responseDto.getCartItems().get(0).getSku());
    }

    @Test
    public void getCart() {
        final CartResponseDto responseDto = cartService.getCart(1L);

        verify(cartRepository, times(1))
                .findById(Mockito.eq(1L));

        assertEquals(2, responseDto.getCartItems().size());
    }

    @Test
    public void checkOut() {
        cartService.checkOut(1L);

        verify(cartRepository, times(1))
                .findById(Mockito.eq(1L));
        verify(cartRepository, times(1))
                .saveAndFlush(Mockito.any(Cart.class));
    }
}