package de.tarent.challenge.store.carts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.tarent.challenge.store.carts.dto.CartItemCreateDto;
import de.tarent.challenge.store.carts.dto.CartItemResponseDto;
import de.tarent.challenge.store.carts.dto.CartResponseDto;
import de.tarent.challenge.store.carts.dto.CartUpsertDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CartController.class)
public class CartControllerTest {

    private static final String SKU1 = "123", SKU2 = "456", NAME = "Test Product";
    private static final BigDecimal PRICE = new BigDecimal(12.99);
    private static final int QUANTITY1 = 42, QUANTITY2 = 10;

    private final ObjectMapper jsonMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CartService cartService;

    @Before
    public void setUp() {
        CartResponseDto testCart = new CartResponseDto(
                Lists.newArrayList(
                        new CartItemResponseDto(SKU1, NAME, QUANTITY1, PRICE, PRICE),
                        new CartItemResponseDto(SKU2, NAME, QUANTITY2, PRICE, PRICE)
                ),
                BigDecimal.valueOf(42 * 12.99 + 10 * 12.99));
        CartResponseDto testCart2 = new CartResponseDto(
                Lists.newArrayList(
                        new CartItemResponseDto(SKU2, NAME, QUANTITY2, PRICE, PRICE)
                ),
                BigDecimal.valueOf(42 * 12.99));

        given(cartService.getCart(1L)).willReturn(testCart);
        given(cartService.getCart(2L)).willReturn(testCart2);
        given(cartService.getCart(999L)).willThrow(NoSuchElementException.class);
        given(cartService.addToCart(eq(999L), Mockito.any(CartItemCreateDto.class)))
                .willThrow(NoSuchElementException.class);
        given(cartService.removeFromCart(eq(999L), anyString()))
                .willThrow(NoSuchElementException.class);
        given(cartService.createCart(Mockito.any(CartUpsertDto.class))).willReturn(1L);
    }

    @Test
    public void createCart() throws Exception {
        final CartUpsertDto testCart = new CartUpsertDto();
        testCart.setCartItems(Lists.newArrayList(new CartItemCreateDto(SKU1, QUANTITY1)));

        mvc.perform(post("/carts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testCart))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().string("Location",
                        startsWith("http://localhost/carts/1")))
        ;

        CartUpsertDto emptyCart = new CartUpsertDto();
        mvc.perform(post("/carts/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(emptyCart))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void patchCart() throws Exception {
        long cartId = 1;

        CartItemCreateDto item = new CartItemCreateDto(SKU2, QUANTITY2);

        mvc.perform(patch("/carts/" + cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(item))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;

        verify(cartService, times(1))
                .addToCart(eq(cartId), Mockito.any(CartItemCreateDto.class));
        verify(cartService, never())
                .removeFromCart(eq(cartId), anyString());

        mvc.perform(patch("/carts/" + 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(item))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(""))
        ;

        //remove item
        item = new CartItemCreateDto(SKU2, 0);
        mvc.perform(patch("/carts/" + cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(item))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;

        verify(cartService, times(1))
                .addToCart(eq(cartId), Mockito.any(CartItemCreateDto.class));
        verify(cartService, times(1))
                .removeFromCart(eq(cartId), anyString());
    }

    @Test
    public void retrieveCart() throws Exception {
        long cartId = 1L;

        mvc.perform(get("/carts/" + cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(675.48)))
                .andExpect(jsonPath("$.cartItems", hasSize(2)))
                .andExpect(jsonPath("$.cartItems[0].sku", is(SKU1)))
                .andExpect(jsonPath("$.cartItems[0].name", is(NAME)))
                .andExpect(jsonPath("$.cartItems[0].quantity", is(QUANTITY1)))
                .andExpect(jsonPath("$.cartItems[0].price", is(PRICE)))
                .andExpect(jsonPath("$.cartItems[1].sku", is(SKU2)))
                .andExpect(jsonPath("$.cartItems[1].name", is(NAME)))
                .andExpect(jsonPath("$.cartItems[1].quantity", is(QUANTITY2)))
                .andExpect(jsonPath("$.cartItems[1].price", is(PRICE)))
        ;
    }

    @Test
    public void checkout() throws Exception {
        mvc.perform(put("/carts/" + 1 + "/checkout"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
        ;

        // unknown id
        doThrow(NoSuchElementException.class)
                .when(cartService).checkOut(999);
        mvc.perform(put("/carts/" + 999 + "/checkout"))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }
}