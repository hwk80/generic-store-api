package de.tarent.challenge.store.products;

import com.google.common.collect.Sets;
import de.tarent.challenge.store.products.dto.ProductCreationDto;
import de.tarent.challenge.store.products.dto.ProductUpdateDto;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProductTest {

    private static final String SKU = "123", NAME = "Test Product";
    private static final Set<String> EANS = Sets.newHashSet("ean1", "ean2", "ean3");

    private static final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void testCreateDtoMapping() {
        ProductCreationDto createDto = new ProductCreationDto();
        createDto.setSku(SKU);
        createDto.setName(NAME);
        createDto.setEans(EANS);

        Product product = modelMapper.map(createDto, Product.class);
        assertEquals(createDto.getSku(), product.getSku());
        assertEquals(createDto.getName(), product.getName());
        assertEquals(createDto.getEans(), product.getEans());

        product = new Product(SKU, NAME, EANS);
        createDto = modelMapper.map(product, ProductCreationDto.class);
        assertEquals(product.getSku(), createDto.getSku());
        assertEquals(product.getName(), createDto.getName());
        assertEquals(product.getEans(), createDto.getEans());
    }

    @Test
    public void testUpdateDtoMapping() {
        ProductUpdateDto updateDto = new ProductUpdateDto();
        updateDto.setName(NAME);
        updateDto.setEans(EANS);

        Product product = modelMapper.map(updateDto, Product.class);
        assertNull(product.getSku());
        assertEquals(updateDto.getName(), product.getName());
        assertEquals(updateDto.getEans(), product.getEans());

        product = new Product(SKU, NAME, EANS);
        updateDto = modelMapper.map(product, ProductUpdateDto.class);
        assertEquals(updateDto.getName(), product.getName());
        assertEquals(updateDto.getEans(), product.getEans());
    }

}