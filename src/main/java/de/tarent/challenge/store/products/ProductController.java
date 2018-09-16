package de.tarent.challenge.store.products;

import de.tarent.challenge.store.products.dto.ProductResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private final ProductService productService;

    @Autowired
    protected ModelMapper modelMapper;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<ProductResponseDto> retrieveProducts() {
        return productService.retrieveAllProducts().stream()
                .map(p -> mapModel(p, ProductResponseDto.class))
                .collect(Collectors.toList())
                ;
    }

    @GetMapping("/{sku}")
    public ProductResponseDto retrieveProductBySku(@PathVariable String sku) {
        return mapModel(productService.retrieveProductBySku(sku), ProductResponseDto.class);
    }

    private <D> D mapModel(Object source, Class<D> destinationType) {
        if (source == null) return null;
        return modelMapper.map(source, destinationType);
    }
}
