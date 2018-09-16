package de.tarent.challenge.store.products;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductCatalogTest {

    private static final String SKU = "123", NAME="Test Product";
    private static final Set<String> EANS = Sets.newHashSet("ean1", "ean2", "ean3");

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductCatalog productCatalog;

    @Before
    public void setUp() {
        Product testProduct = new Product(SKU, NAME, EANS);

        entityManager.persist(testProduct);
        entityManager.flush();
    }

    @Test
    public void findBySku() {
        Product found = productCatalog.findBySku(SKU);

        assertNotNull(found);
        assertEquals(SKU, found.getSku());
        assertEquals(NAME, found.getName());
        assertArrayEquals(EANS.toArray(), found.getEans().toArray());
    }
}