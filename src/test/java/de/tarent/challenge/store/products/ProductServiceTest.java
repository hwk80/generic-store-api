package de.tarent.challenge.store.products;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.tarent.challenge.store.products.dto.ProductCreationDto;
import de.tarent.challenge.store.products.dto.ProductResponseDto;
import de.tarent.challenge.store.products.dto.ProductUpdateDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

    private static final String SKU = "123", NAME = "Test Product";
    private static final Set<String> EANS = Sets.newHashSet("ean1", "ean2", "ean3");
    private static final BigDecimal PRICE = new BigDecimal(12.99);

    @MockBean
    private ProductCatalog productCatalog;

    private ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductService(productCatalog);

        Product testProduct = new Product(SKU, NAME, EANS, PRICE);

        given(productCatalog.findBySku(SKU)).willReturn(testProduct);
        given(productCatalog.findAll()).willReturn(Lists.newArrayList(testProduct));
    }

    @Test
    public void retrieveAllProducts() {
        List<ProductResponseDto> found = productService.retrieveAllProducts();

        assertEquals(1, found.size());
        assertEquals(new ProductResponseDto(SKU, NAME, EANS, PRICE, true).hashCode(), found.get(0).hashCode());
    }

    @Test
    public void retrieveAllProductsEmpty() {
        given(productCatalog.findAll()).willReturn(new ArrayList<>());

        List<ProductResponseDto> found = productService.retrieveAllProducts();

        assertEquals(0, found.size());
    }

    @Test
    public void retrieveProductBySku() {
        ProductResponseDto found = productService.retrieveProductBySku(SKU);

        assertEquals(new ProductResponseDto(SKU, NAME, EANS, PRICE, true).hashCode(), found.hashCode());
    }

    @Test
    public void createProduct() {
        productService.createProduct(new ProductCreationDto(SKU, NAME, EANS, PRICE));

        verify(productCatalog, times(1)).saveAndFlush(Mockito.any(Product.class));
    }

    @Test
    public void updateProduct() {
        productService.updateProduct(SKU, new ProductUpdateDto(NAME, EANS, PRICE));
        verify(productCatalog, times(1)).findBySku(SKU);
        verify(productCatalog, times(1)).saveAndFlush(Mockito.any(Product.class));
    }

    @Test(expected = NoSuchElementException.class)
    public void updateProductFail() {
        given(productCatalog.findBySku("unknown sku")).willReturn(null);
        try {
            productService.updateProduct("unknown sku", new ProductUpdateDto(NAME, EANS, PRICE));
        } finally {
            verify(productCatalog, times(1)).findBySku("unknown sku");
            verify(productCatalog, never()).save(Mockito.any(Product.class));
        }
    }
}