package de.tarent.challenge.store.products;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

    private static final String SKU = "123", NAME="Test Product";
    private static final Set<String> EANS = Sets.newHashSet("ean1", "ean2", "ean3");

    @MockBean
    private ProductCatalog productCatalog;

    private ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductService(productCatalog);

        Product testProduct = new Product(SKU, NAME, EANS);

        Mockito.when(productCatalog.findBySku(SKU)).thenReturn(testProduct);

        Mockito.when(productCatalog.findAll()).thenReturn(Lists.newArrayList(testProduct));
    }

    @Test
    public void retrieveAllProducts() {
        List<Product> found = productService.retrieveAllProducts();

        assertEquals(1, found.size());
        assertEquals(new Product(SKU, NAME, EANS).hashCode(), found.get(0).hashCode());
    }

    @Test
    public void retrieveProductBySku() {
        Product found = productService.retrieveProductBySku(SKU);

        assertEquals(new Product(SKU, NAME, EANS).hashCode(), found.hashCode());
    }
}