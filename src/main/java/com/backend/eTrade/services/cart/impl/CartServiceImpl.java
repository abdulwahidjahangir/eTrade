package com.backend.eTrade.services.cart.impl;

import com.backend.eTrade.error.carts.CartException;
import com.backend.eTrade.error.products.ProductDetailException;
import com.backend.eTrade.error.user.UserException;
import com.backend.eTrade.models.cart.Cart;
import com.backend.eTrade.models.cart.CartItem;
import com.backend.eTrade.models.order.ShippingInfo;
import com.backend.eTrade.models.products.ProductDetail;
import com.backend.eTrade.models.products.ProductImage;
import com.backend.eTrade.models.users.User;
import com.backend.eTrade.repositories.carts.CartItemRepository;
import com.backend.eTrade.repositories.carts.CartRepository;
import com.backend.eTrade.repositories.order.ShippingInfoRepository;
import com.backend.eTrade.repositories.users.UserRepository;
import com.backend.eTrade.services.cart.CartService;
import com.backend.eTrade.services.cart.dtos.CartDTO;
import com.backend.eTrade.services.cart.dtos.CartItemDTO;
import com.backend.eTrade.services.order.OrderService;
import com.backend.eTrade.services.products.ProductDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private OrderService orderService;

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Override
    public CartDTO getMyCart(String cartIdentifier, String userEmail) {
        Cart cart = retrieveOrCreateCart(cartIdentifier, userEmail);
        if (cart == null) {
            return null;
        }
        return convertCartIntoCartDTO(cart);
    }

    @Override
    public CartDTO addItemToCart(String cartIdentifier, String userEmail, Long itemId, int quantity) {
        validateQuantity(quantity);

        Cart cart = retrieveOrCreateCart(cartIdentifier, userEmail);

        if (cart.getUser() != null && !cart.getUser().getEmail().equals(userEmail)) {
            throw new CartException(String.format("Invalid Cart Identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST);
        }

        ProductDetail productDetail = productDetailService.getProductDetailById(itemId)
                .orElseThrow(() -> new ProductDetailException(String.format("No product found with id '%s'", itemId), HttpStatus.BAD_REQUEST));

        if (productDetail.getInventory() < quantity) {
            throw new CartException("Adding quantity must be less than or equal to available quantity", HttpStatus.BAD_REQUEST);
        }

        CartItem cartItem = cartItemRepository.findByCartAndProductDetail(cart, productDetail)
                .orElse(new CartItem());

        if (cartItem.getQuantity() + quantity < productDetail.getProduct().getMinQuantityToBuy()) {
            throw new CartException("You must match min quantity to buy in order to proceed", HttpStatus.BAD_REQUEST);
        }

        cartItem.setCart(cart);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setProductDetail(productDetail);

        cartItemRepository.save(cartItem);

        cart = cartRepository.findByIdentifier(cart.getIdentifier())
                .orElseThrow(() -> new CartException("An unexpected error occurred while retrieving the cart", HttpStatus.INTERNAL_SERVER_ERROR));
        return convertCartIntoCartDTO(cart);
    }

    @Override
    public CartDTO updateItemQuantityToCart(String cartIdentifier, Long itemId, int quantity) {
        validateQuantity(quantity);

        if (!isValidUUID(cartIdentifier)) {
            throw new CartException(String.format("Invalid cart identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST);
        }

        System.out.println(cartIdentifier);
        Cart cart = getCart(UUID.fromString(cartIdentifier), "");
        if (cart == null) {
            throw new CartException(String.format("Cart not found for identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST);
        }

        ProductDetail productDetail = productDetailService.getProductDetailById(itemId)
                .orElseThrow(() -> new ProductDetailException(String.format("No product found with id '%s'", itemId), HttpStatus.BAD_REQUEST));

        if (productDetail.getInventory() < quantity) {
            throw new CartException("Adding quantity must be less than or equal to available quantity", HttpStatus.BAD_REQUEST);
        }

        if (quantity < productDetail.getProduct().getMinQuantityToBuy()) {
            throw new CartException("You must match min quantity to buy in order to proceed", HttpStatus.BAD_REQUEST);
        }

        CartItem cartItem = cartItemRepository.findByCartAndProductDetail(cart, productDetail)
                .orElseThrow(() -> new CartException(String.format("No such item with id '%s' in cart ", itemId), HttpStatus.BAD_REQUEST));

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        cart = cartRepository.findByIdentifier(cart.getIdentifier())
                .orElseThrow(() -> new CartException("An unexpected error occurred while retrieving the cart", HttpStatus.INTERNAL_SERVER_ERROR));

        return convertCartIntoCartDTO(cart);
    }

    @Override
    public CartDTO removeItemFromCart(String cartIdentifier, Long itemId) {
        if (!isValidUUID(cartIdentifier)) {
            throw new CartException(String.format("Invalid cart identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST);
        }

        Cart cart = getCart(UUID.fromString(cartIdentifier), null);
        if (cart == null) {
            throw new CartException(String.format("Cart not found for identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST);
        }

        ProductDetail productDetail = productDetailService.getProductDetailById(itemId)
                .orElseThrow(() -> new ProductDetailException(String.format("No product found with id '%s'", itemId), HttpStatus.BAD_REQUEST));

        cartItemRepository.deleteCartItem(cart, productDetail);

        cart = cartRepository.findByIdentifier(cart.getIdentifier())
                .orElseThrow(() -> new CartException("An unexpected error occurred while retrieving the cart", HttpStatus.INTERNAL_SERVER_ERROR));

        return convertCartIntoCartDTO(cart);
    }

    @Async
    @Override
    public void updateCartAfterUserLogIn(String cartIdentifier, String userEmail) {
        if (!isValidUUID(cartIdentifier)) {
            return;
        }

        System.out.println(cartIdentifier);

        Cart guestCart = getCart(UUID.fromString(cartIdentifier), "");
        Cart userCart = getCart(null, userEmail);

        if (guestCart == null) {
            return;
        }

        if (userCart == null) {
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != null) {
                guestCart.setUser(user);
                cartRepository.save(guestCart);
            }
            return;
        }

        List<CartItem> cartItems = cartItemRepository.findByCart(guestCart);

        for (CartItem item : cartItems) {
            item.setCart(userCart);
            cartItemRepository.save(item);
        }

        cartRepository.delete(guestCart);
    }

    @Async
    @Override
    public void addPaymentToOrder(String cartIdentifier) {
        if (!isValidUUID(cartIdentifier)) {
            throw new CartException(String.format("Invalid cart identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST);
        }

        Cart cart = cartRepository.findByIdentifier(UUID.fromString(cartIdentifier))
                .orElseThrow(() -> new CartException(String.format("Invalid cart identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST));

        if (cart.getShippingInfo() == null) {
            throw new CartException("Shipping Info are null", HttpStatus.BAD_REQUEST);
        }

        ShippingInfo shippingInfo = cart.getShippingInfo();
        shippingInfo.setPaymentMethod("BANK_TRANSFER");
        shippingInfo.setPaymentId("AWAITING_FOR_PAYMENT");
        shippingInfoRepository.save(shippingInfo);

        cart = cartRepository.findByIdentifier(cart.getIdentifier())
                .orElseThrow(() -> new CartException("An unexpected error occurred while retrieving the cart", HttpStatus.INTERNAL_SERVER_ERROR));

        orderService.createOrder(cart, "AWAITING_FOR_PAYMENT");
    }

    private Cart retrieveOrCreateCart(String cartIdentifier, String userEmail) {
        Cart cart = null;
        if (userEmail != null) {
            cart = getCart(null, userEmail);
            if (cart == null) {
                cart = createNewCart(userEmail);
            }
        } else {
            if (isValidUUID(cartIdentifier)) {
                cart = getCart(UUID.fromString(cartIdentifier), null);
            } else {
                cart = createNewCart(null);
            }
        }
        return cart;
    }

    private Cart getCart(UUID cartIdentifier, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);

        if (user != null) {
            if (!user.isVerified()) {
                throw new UserException("Please verify your account to continue", HttpStatus.BAD_REQUEST);
            }

            if (user.isBlocked()) {
                throw new UserException("This account has been blocked, please contact support", HttpStatus.BAD_REQUEST);
            }

            return cartRepository.findByUser(user).orElse(null);
        } else if (cartIdentifier != null) {
            return cartRepository.findByIdentifier(cartIdentifier).orElse(null);
        }

        return null;
    }

    private Cart createNewCart(String userEmail) {
        Cart cart = new Cart();
        cart.setIdentifier(getNewCartId());

        if (userEmail != null) {
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != null) {
                if (!user.isVerified()) {
                    throw new UserException("Please verify your account to continue", HttpStatus.BAD_REQUEST);
                }
                if (user.isBlocked()) {
                    throw new UserException("This account has been blocked, please contact support", HttpStatus.BAD_REQUEST);
                }
                cart.setUser(user);
            }
        }

        return cartRepository.save(cart);
    }

    private UUID getNewCartId() {
        return UUID.randomUUID();
    }

    public boolean isValidUUID(String uuid) {
        return uuid != null && UUID_PATTERN.matcher(uuid).matches();
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new CartException("Quantity cannot be less than or equal to 0", HttpStatus.BAD_REQUEST);
        }
    }

    public CartDTO convertCartIntoCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        if (cart.getUser() != null) {
            cartDTO.setUser(cart.getUser().getEmail());
        }
        cartDTO.setIdentifier(cart.getIdentifier());

        List<CartItemDTO> cartItemDTOS = new ArrayList<>();

        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                CartItemDTO cartItemDTO = new CartItemDTO();
                cartItemDTO.setProductId(item.getProductDetail().getId());
                cartItemDTO.setTitle(item.getProductDetail().getProduct().getTitle());
                cartItemDTO.setSlug(item.getProductDetail().getProduct().getSlug());

                String imageUrl = "";

                if (!item.getProductDetail().getProduct().getImages().isEmpty()) {
                    for (ProductImage image : item.getProductDetail().getProduct().getImages()) {
                        if (image.getColor().getId().equals(item.getProductDetail().getColor().getId())) {
                            imageUrl = image.getUrl();
                            break;
                        }
                    }

                    if (imageUrl.isEmpty()) {
                        imageUrl = item.getProductDetail().getProduct().getImages().iterator().next().getUrl();
                    }
                } else {
                    imageUrl = "";
                }
                cartItemDTO.setImage(imageUrl);

                cartItemDTO.setColor(item.getProductDetail().getColor().getColorName());
                cartItemDTO.setColorCode(item.getProductDetail().getColor().getColorHexCode());
                cartItemDTO.setSize(item.getProductDetail().getSize().getSize());
                cartItemDTO.setInventory(item.getProductDetail().getInventory());
                cartItemDTO.setPrice(item.getProductDetail().getPrice());
                cartItemDTO.setDiscount(item.getProductDetail().getDiscount());
                cartItemDTO.setDiscountedPrice(item.getProductDetail().getDiscountedPrice());
                cartItemDTO.setSelectedQuantity(item.getQuantity());
                cartItemDTOS.add(cartItemDTO);
            }

            BigDecimal totalWithoutShipping = BigDecimal.ZERO;
            for (CartItemDTO cartItemDTO : cartItemDTOS) {
                BigDecimal itemTotal = cartItemDTO.getDiscountedPrice().multiply(BigDecimal.valueOf(cartItemDTO.getSelectedQuantity()));
                totalWithoutShipping = totalWithoutShipping.add(itemTotal);
            }
            totalWithoutShipping = totalWithoutShipping.setScale(2, RoundingMode.HALF_UP);
            cartDTO.setTotalWithoutShipping(totalWithoutShipping);

            BigDecimal threshold = new BigDecimal("30.00");
            BigDecimal shippingCost = new BigDecimal("7.99");
            if (totalWithoutShipping.compareTo(threshold) < 0) {
                cartDTO.setShippingPrice(shippingCost);
                cartDTO.setTotalWithShipping(totalWithoutShipping.add(shippingCost));
            } else {
                cartDTO.setShippingPrice(BigDecimal.ZERO);
                cartDTO.setTotalWithShipping(totalWithoutShipping);
            }

            cartDTO.setCartItems(cartItemDTOS);
            cartDTO.setShippingInfo(cart.getShippingInfo());
        }

        return cartDTO;
    }
}
