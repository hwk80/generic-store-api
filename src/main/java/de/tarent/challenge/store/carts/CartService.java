package de.tarent.challenge.store.carts;

import de.tarent.challenge.store.carts.dto.CartItemCreateDto;
import de.tarent.challenge.store.carts.dto.CartResponseDto;
import de.tarent.challenge.store.carts.dto.CartUpsertDto;
import de.tarent.challenge.store.products.ProductService;
import de.tarent.challenge.store.products.dto.ProductResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductService productService;
    private ModelMapper modelMapper = new ModelMapper();

    public CartService() {
    }

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Long createCart(CartUpsertDto dto) {
        final Cart cart = modelMapper.map(dto, Cart.class);
        cartRepository.saveAndFlush(cart);
        return cart.getId();
    }

    public CartResponseDto addToCart(long cartId, CartItemCreateDto cartItemCreateDto) {
        final Cart cart = cartRepository.findById(cartId).get();
        final ProductResponseDto product = productService.retrieveProductBySku(cartItemCreateDto.getSku());
        if (cart == null || product == null) {
            throw new NoSuchElementException();
        }
        CartItem cartItem = new CartItem();
        cartItem.setPrice(product.getPrice());
        cartItem.setQuantity(cartItemCreateDto.getQuantity());

        final List<CartItem> cartItems = cart.getCartItems();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        cartRepository.saveAndFlush(cart);
        return modelMapper.map(cart, CartResponseDto.class);
    }

    /**
     * Remove item from cart. If the cart is empty after this operation, the cart is deleted.
     *
     * @param cartId
     * @param sku
     * @return The updated cart or null if it was deleted.
     */
    public CartResponseDto removeFromCart(long cartId, String sku) {
        final Cart cart = cartRepository.findById(cartId).get();
        if (cart == null) {
            throw new NoSuchElementException();
        }
        final List<CartItem> cartItems = cart.getCartItems();
        cartItems.removeIf(i -> i.getSku().equals(sku));

        if (cartItems.isEmpty()) {
            cartRepository.delete(cart);
            return null;
        }

        cart.setCartItems(cartItems);
        cartRepository.saveAndFlush(cart);
        return modelMapper.map(cart, CartResponseDto.class);
    }

    public CartResponseDto getCart(long cartId) {
        final Optional<Cart> found = cartRepository.findById(cartId);
        if (!found.isPresent()) {
            return null;
        }
        CartResponseDto responseDto = modelMapper.map(found.get(), CartResponseDto.class);
        return responseDto;
    }

    public void checkOut(long cartId) {
        final Cart cart = cartRepository.findById(cartId).get();
        if (cart == null) {
            throw new NoSuchElementException();
        }
        cart.setCheckedOut();

        cartRepository.saveAndFlush(cart);
    }
}
