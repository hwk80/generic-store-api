package de.tarent.challenge.store.carts;

import de.tarent.challenge.store.carts.dto.CartItemCreateDto;
import de.tarent.challenge.store.carts.dto.CartResponseDto;
import de.tarent.challenge.store.carts.dto.CartUpsertDto;
import de.tarent.challenge.store.exceptions.ProductNotAvailableException;
import de.tarent.challenge.store.products.ProductService;
import de.tarent.challenge.store.products.dto.ProductResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
        final List<CartItem> cartItems = dto.getCartItems().stream()
                .map(this::createCartItem)
                .collect(Collectors.toList());

        final Cart cart = modelMapper.map(dto, Cart.class);
        cart.setCartItems(cartItems);
        cartRepository.saveAndFlush(cart);
        return cart.getId();
    }

    public CartResponseDto addToCart(long cartId, CartItemCreateDto cartItemCreateDto) {
        final Cart cart = cartRepository.findById(cartId).get();
        if (cart == null) {
            throw new NoSuchElementException();
        }

        cart.checkLifeCycleState();
        CartItem cartItem = createCartItem(cartItemCreateDto);

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
        cart.checkLifeCycleState();

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
            throw new NoSuchElementException();
        }
        CartResponseDto responseDto = modelMapper.map(found.get(), CartResponseDto.class);
        return responseDto;
    }

    public List<CartResponseDto> retrieveAllCarts() {
        return cartRepository.findAll().stream()
                .map(p -> mapModel(p, CartResponseDto.class))
                .collect(Collectors.toList())
                ;
    }

    public void checkOut(long cartId) {
        final Cart cart = cartRepository.findById(cartId).get();
        if (cart == null) {
            throw new NoSuchElementException();
        }
        cart.setCheckedOut();

        cartRepository.saveAndFlush(cart);
    }

    private CartItem createCartItem(CartItemCreateDto dto) {
        final ProductResponseDto product = productService.retrieveProductBySku(dto.getSku());
        if (product == null || !product.isAvailable()) {
            throw new ProductNotAvailableException();
        }

        CartItem cartItem = new CartItem();
        cartItem.setSku(product.getSku());
        cartItem.setPrice(product.getPrice());
        cartItem.setQuantity(dto.getQuantity());

        return cartItem;
    }

    private <D> D mapModel(Object source, Class<D> destinationType) {
        if (source == null) return null;
        return modelMapper.map(source, destinationType);
    }
}
