package de.tarent.challenge.store.products;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductCatalogTest {

    private static final String SKU = "123", NAME = "Test Product";
    private static final Set<String> EANS = Sets.newHashSet("ean1", "ean2", "ean3");
    private static final BigDecimal PRICE = new BigDecimal(12.99);

    @Autowired
    private ProductCatalog productCatalog;

    @Before
    public void setUp() {
        productCatalog.deleteAll();
        Product testProduct = new Product(SKU, NAME, EANS, PRICE);
        productCatalog.saveAndFlush(testProduct);
    }

    @Test
    public void findBySku() {
        Product found = productCatalog.findBySku(SKU);

        assertNotNull(found);
        assertEquals(SKU, found.getSku());
        assertEquals(NAME, found.getName());
        assertArrayEquals(EANS.toArray(), found.getEans().toArray());
    }

    @Test
    public void testInsert() {
        Product testProduct = new Product(SKU + "_new", NAME, EANS, PRICE);

        productCatalog.saveAndFlush(testProduct);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testDuplicateInsertFail() {
        // given a product with an already existent SKU
        Product testProduct = new Product(SKU, NAME, EANS, PRICE);

        productCatalog.saveAndFlush(testProduct);
    }

    @Test
    public void testUpdate() {
        Product product = productCatalog.findBySku(SKU);
        product.setName("Product got a new Name!");

        Product saved = productCatalog.saveAndFlush(product);
        assertEquals(product, saved);
    }
}