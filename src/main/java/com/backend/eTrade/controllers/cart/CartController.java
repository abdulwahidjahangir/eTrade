package com.backend.eTrade.controllers.cart;

import com.backend.eTrade.models.order.ShippingInfo;
import com.backend.eTrade.requests.order.ShippingInfoRequest;
import com.backend.eTrade.security.service.UserDetailsImpl;
import com.backend.eTrade.services.cart.CartService;
import com.backend.eTrade.services.cart.dtos.CartDTO;
import com.backend.eTrade.services.order.ShippingInfoService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ShippingInfoService shippingInfoService;

    @GetMapping("/my-cart")
    public ResponseEntity<?> getMyCart(
            @RequestHeader(value = "Cart-Identifier", required = false) String cartIdentifier,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String userEmail = userDetails != null ? userDetails.getEmail() : null;
        CartDTO cartDTO = cartService.getMyCart(cartIdentifier, userEmail);

        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/add-item-to-cart")
    public ResponseEntity<?> addItemTOCart(
            @NotNull @RequestParam Long itemId,
            @NotNull @RequestParam int quantity,
            @RequestHeader(value = "Cart-Identifier", required = false) String cartIdentifier,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String userEmail = userDetails != null ? userDetails.getEmail() : null;
        CartDTO cartDTO = cartService.addItemToCart(cartIdentifier, userEmail, itemId, quantity);

        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/update-item-quantity-in-cart")
    public ResponseEntity<?> updateItemQuantityInCart(
            @NotNull @RequestParam Long itemId,
            @NotNull @RequestParam int quantity,
            @RequestHeader(value = "Cart-Identifier", required = false) String cartIdentifier
    ) {
        CartDTO cartDTO = cartService.updateItemQuantityToCart(cartIdentifier, itemId, quantity);

        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/remove-item-from-cart")
    public ResponseEntity<?> removeItemFromCart(
            @NotNull @RequestParam Long itemId,
            @RequestHeader(value = "Cart-Identifier", required = false) String cartIdentifier
    ) {
        CartDTO cartDTO = cartService.removeItemFromCart(cartIdentifier, itemId);

        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/shipping-info-for-cart")
    public ResponseEntity<?> ShippingInfoForCart(
            @RequestHeader(value = "Cart-Identifier", required = false) String cartIdentifier,
            @RequestBody ShippingInfoRequest shippingInfoRequest
    ) {
        ShippingInfo shippingInfo = shippingInfoService.updateAddressToCart(cartIdentifier, shippingInfoRequest);

        return ResponseEntity.ok(shippingInfo);
    }

    @PutMapping("/create-order")
    public ResponseEntity<?> removeItemFromCart(
            @RequestHeader(value = "Cart-Identifier", required = false) String cartIdentifier
    ) {
        cartService.addPaymentToOrder(cartIdentifier);

        return ResponseEntity.ok("We have sent you order confirmation email");
    }
}
