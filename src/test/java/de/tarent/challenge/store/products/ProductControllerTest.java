package de.tarent.challenge.store.products;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.tarent.challenge.store.products.dto.ProductCreationDto;
import de.tarent.challenge.store.products.dto.ProductResponseDto;
import de.tarent.challenge.store.products.dto.ProductUpdateDto;
import org.hibernate.exception.ConstraintViolationException;
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
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private static final String SKU = "123", NAME = "Test Product";
    private static final Set<String> EANS = Sets.newHashSet("ean1", "ean2", "ean3");
    private static final BigDecimal PRICE = new BigDecimal(12.99);

    private final ObjectMapper jsonMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Before
    public void setUp() {
        ProductResponseDto testProduct = new ProductResponseDto(SKU, NAME, EANS, PRICE);
        ProductResponseDto testProduct2 = new ProductResponseDto(SKU + "2", NAME + " 2", EANS, PRICE);

        List<ProductResponseDto> allOrders = Arrays.asList(testProduct, testProduct2);

        given(productService.retrieveAllProducts()).willReturn(allOrders);
        given(productService.retrieveProductBySku(SKU)).willReturn(testProduct);
        given(productService.retrieveProductBySku(SKU + "2")).willReturn(testProduct2);
    }

    @Test
    public void retrieveProducts() throws Exception {

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].sku", is(SKU)))
                .andExpect(jsonPath("$[0].name", is(NAME)))
                .andExpect(jsonPath("$[0].eans[0]", isIn(EANS.toArray())))
                .andExpect(jsonPath("$[0].eans[1]", isIn(EANS.toArray())))
                .andExpect(jsonPath("$[0].eans[2]", isIn(EANS.toArray())))

                .andExpect(jsonPath("$[1].sku", is(SKU + "2")))
                .andExpect(jsonPath("$[1].name", is(NAME + " 2")))
                .andExpect(jsonPath("$[1].eans[0]", isIn(EANS.toArray())))
                .andExpect(jsonPath("$[1].eans[1]", isIn(EANS.toArray())))
                .andExpect(jsonPath("$[1].eans[2]", isIn(EANS.toArray())))
        ;
    }

    @Test
    public void retrieveProductsEmpty() throws Exception {
        given(productService.retrieveAllProducts()).willReturn(Lists.newArrayList());

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
        ;
    }

    @Test
    public void retrieveProductBySku() throws Exception {
        mvc.perform(get("/products/" + SKU))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is(SKU)))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.eans[0]", isIn(EANS.toArray())))
                .andExpect(jsonPath("$.eans[1]", isIn(EANS.toArray())))
                .andExpect(jsonPath("$.eans[2]", isIn(EANS.toArray())))
        ;
    }

    @Test
    public void retrieveProductBySkuFail() throws Exception {
        mvc.perform(get("/products/" + "unknownSku"))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
        ;
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductCreationDto testProduct = new ProductCreationDto(SKU, NAME, EANS, PRICE);

        mvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testProduct))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().string("Location",
                        is("http://localhost/products/" + SKU)))
        ;

        // missing name
        testProduct.setName(null);
        mvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testProduct))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        // existing SKU
        testProduct.setName(NAME);
        doThrow(ConstraintViolationException.class)
                .when(productService).createProduct(Mockito.any(ProductCreationDto.class));
        mvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testProduct))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductUpdateDto testProduct = new ProductUpdateDto(NAME, EANS, PRICE);

        mvc.perform(put("/products/" + SKU)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testProduct))
        )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
        ;
    }

    @Test
    public void testUpdateProductFail() throws Exception {
        doThrow(NoSuchElementException.class)
                .when(productService).updateProduct(eq("unknownSku"), Mockito.any(ProductUpdateDto.class));

        ProductUpdateDto testProduct = new ProductUpdateDto();
        testProduct.setName(NAME);
        testProduct.setEans(EANS);
        testProduct.setPrice(PRICE);

        mvc.perform(put("/products/" + "unknownSku")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testProduct))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(""))
        ;
    }
}