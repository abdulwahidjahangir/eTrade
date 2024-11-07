package com.backend.eTrade.services.order;

import com.backend.eTrade.models.order.ShippingInfo;
import com.backend.eTrade.requests.order.ShippingInfoRequest;

public interface ShippingInfoService {
    ShippingInfo updateAddressToCart(String cartIdentifier, ShippingInfoRequest shippingInfoRequest);
}
