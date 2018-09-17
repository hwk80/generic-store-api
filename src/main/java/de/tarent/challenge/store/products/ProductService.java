package de.tarent.challenge.store.products;

import de.tarent.challenge.store.products.dto.ProductCreationDto;
import de.tarent.challenge.store.products.dto.ProductResponseDto;
import de.tarent.challenge.store.products.dto.ProductUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private final ProductCatalog productCatalog;

    private ModelMapper modelMapper = new ModelMapper();

    public ProductService(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public List<ProductResponseDto> retrieveAllProducts() {
        return productCatalog.findAll().stream()
                .map(p -> mapModel(p, ProductResponseDto.class))
                .collect(Collectors.toList())
                ;
    }

    public ProductResponseDto retrieveProductBySku(String sku) {
        return mapModel(productCatalog.findBySku(sku), ProductResponseDto.class);
    }

    public void createProduct(ProductCreationDto product) {
        productCatalog.saveAndFlush(mapModel(product, Product.class));
    }

    public void updateProduct(String sku, ProductUpdateDto updateDto) {
        final Product found = productCatalog.findBySku(sku);
        if (found == null) {
            throw new NoSuchElementException();
        }

        found.setSku(sku);
        found.setName(updateDto.getName());
        found.setEans(updateDto.getEans());

        productCatalog.saveAndFlush(found);
    }

    private <D> D mapModel(Object source, Class<D> destinationType) {
        if (source == null) return null;
        return modelMapper.map(source, destinationType);
    }
}
