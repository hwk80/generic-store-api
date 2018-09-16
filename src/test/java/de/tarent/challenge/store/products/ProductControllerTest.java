package de.tarent.challenge.store.products;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private static final String SKU = "123", NAME="Test Product";
    private static final Set<String> EANS = Sets.newHashSet("ean1", "ean2", "ean3");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Before
    public void setUp() {
        Product testProduct = new Product(SKU, NAME, EANS);
        Product testProduct2 = new Product(SKU+"2", NAME+" 2", EANS);

        List<Product> allOrders = Arrays.asList(testProduct, testProduct2);

        given(productService.retrieveAllProducts()).willReturn(allOrders);
        given(productService.retrieveProductBySku(SKU)).willReturn(testProduct);
        given(productService.retrieveProductBySku(SKU+"2")).willReturn(testProduct2);
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

                .andExpect(jsonPath("$[1].sku", is(SKU+"2")))
                .andExpect(jsonPath("$[1].name", is(NAME+" 2")))
                .andExpect(jsonPath("$[1].eans[0]", isIn(EANS.toArray())))
                .andExpect(jsonPath("$[1].eans[1]", isIn(EANS.toArray())))
                .andExpect(jsonPath("$[1].eans[2]", isIn(EANS.toArray())))
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
}