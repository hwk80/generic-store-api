package de.tarent.challenge.store.carts;

import de.tarent.challenge.store.carts.dto.CartItemCreateDto;
import de.tarent.challenge.store.carts.dto.CartResponseDto;
import de.tarent.challenge.store.carts.dto.CartUpsertDto;
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
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public CartResponseDto retrieveProductBySku(@PathVariable Long id) {
        return cartService.getCart(id);
    }

    @PostMapping
    public ResponseEntity<Object> createCart(@Valid @RequestBody CartUpsertDto cartDto) {
        final long id = cartService.createCart(cartDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/" + id)
                .build().toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CartResponseDto> patchCart(@PathVariable long id, @Valid @RequestBody CartItemCreateDto dto) {
        CartResponseDto responseDto;
        if (dto.getQuantity() == 0) {
            responseDto = cartService.removeFromCart(id, dto.getSku());
        } else {
            responseDto = cartService.addToCart(id, dto);
        }
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No cart found for this id.")
    public void handleResourceNotFoundException() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The cart already contains an item with this SKU.")
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleDuplicateKeyException() {
    }

}
