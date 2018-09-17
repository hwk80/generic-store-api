package de.tarent.challenge.store.products;

import de.tarent.challenge.store.products.dto.ProductCreationDto;
import de.tarent.challenge.store.products.dto.ProductResponseDto;
import de.tarent.challenge.store.products.dto.ProductUpdateDto;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<ProductResponseDto> retrieveProducts() {
        return productService.retrieveAllProducts();
    }

    @GetMapping("/{sku}")
    public ProductResponseDto retrieveProductBySku(@PathVariable String sku) {
        return productService.retrieveProductBySku(sku);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductCreationDto product) {
        productService.createProduct(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/" + product.getSku())
                .build().toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{sku}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable String sku, @Valid @RequestBody ProductUpdateDto product) {
        productService.updateProduct(sku, product);
    }

    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No product found for this SKU.")
    public void handleResourceNotFoundException() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "A product already exists for this SKU.")
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleDuplicateKeyException() {
    }
}
