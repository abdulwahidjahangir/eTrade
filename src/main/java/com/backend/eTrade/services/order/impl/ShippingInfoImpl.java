package com.backend.eTrade.services.order.impl;

import com.backend.eTrade.error.carts.CartException;
import com.backend.eTrade.models.cart.Cart;
import com.backend.eTrade.models.order.ShippingInfo;
import com.backend.eTrade.repositories.carts.CartRepository;
import com.backend.eTrade.repositories.order.ShippingInfoRepository;
import com.backend.eTrade.requests.order.ShippingInfoRequest;
import com.backend.eTrade.services.order.ShippingInfoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
public class ShippingInfoImpl implements ShippingInfoService {

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private CartRepository cartRepository;

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Override
    public ShippingInfo updateAddressToCart(String cartIdentifier, ShippingInfoRequest shippingInfoRequest) {
        if (!isValidUUID(cartIdentifier)) {
            throw new CartException(String.format("Invalid cart identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST);
        }

        Cart cart = cartRepository.findByIdentifier(UUID.fromString(cartIdentifier))
                .orElseThrow(() -> new CartException(String.format("Invalid cart identifier '%s'", cartIdentifier), HttpStatus.BAD_REQUEST));

        ShippingInfo shippingInfo = getShippingInfo(shippingInfoRequest, cart);

        shippingInfo = shippingInfoRepository.save(shippingInfo);
        cart.setShippingInfo(shippingInfo);
        cartRepository.save(cart);

        return shippingInfo;
    }

    private static ShippingInfo getShippingInfo(ShippingInfoRequest shippingInfoRequest, Cart cart) {
        ShippingInfo shippingInfo = cart.getShippingInfo();
        if (shippingInfo == null) {
            shippingInfo = new ShippingInfo();
        }
        shippingInfo.setFirstName(shippingInfoRequest.getFirstName());
        shippingInfo.setLastName(shippingInfoRequest.getLastName());
        shippingInfo.setEmail(shippingInfoRequest.getEmail());
        shippingInfo.setPhone(shippingInfoRequest.getPhone());
        shippingInfo.setStreet1(shippingInfoRequest.getStreet1());
        shippingInfo.setStreet2(shippingInfoRequest.getStreet2());
        shippingInfo.setCity(shippingInfoRequest.getCity());
        shippingInfo.setCap(shippingInfoRequest.getCap());
        shippingInfo.setCountry(shippingInfoRequest.getCountry());
        return shippingInfo;
    }

    public boolean isValidUUID(String uuid) {
        return uuid != null && UUID_PATTERN.matcher(uuid).matches();
    }

}
