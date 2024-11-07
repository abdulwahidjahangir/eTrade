package com.backend.eTrade.services.order.impl;

import com.backend.eTrade.error.orders.OrderException;
import com.backend.eTrade.error.user.UserException;
import com.backend.eTrade.models.cart.Cart;
import com.backend.eTrade.models.cart.CartItem;
import com.backend.eTrade.models.order.Order;
import com.backend.eTrade.models.order.OrderItem;
import com.backend.eTrade.models.products.ProductImage;
import com.backend.eTrade.models.users.User;
import com.backend.eTrade.repositories.carts.CartRepository;
import com.backend.eTrade.repositories.order.OrderItemRepository;
import com.backend.eTrade.repositories.order.OrderRepository;
import com.backend.eTrade.repositories.users.UserRepository;
import com.backend.eTrade.services.order.OrderService;
import com.backend.eTrade.services.order.dtos.OrderDTO;
import com.backend.eTrade.services.order.dtos.OrderItemDTO;
import com.backend.eTrade.utils.EmailService;
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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Async
    @Override
    public void createOrder(Cart cart, String orderStatus) {
        System.out.println("Working");
        Order order = new Order();
        order.setIdentifier(cart.getIdentifier());
        order.setShippingInfo(cart.getShippingInfo());
        order.setOrderStatus(orderStatus);
        if (cart.getUser() != null) {
            order.setUser(cart.getUser());
        }
        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductDetail(cartItem.getProductDetail());
            orderItem.setPrice(cartItem.getProductDetail().getDiscountedPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItemRepository.save(orderItem);
        }

        cartRepository.delete(cart);
//
//        OrderDTO orderDTO = convertOrderIntoOrderDTO(order);

//        emailService.sendOrderConfirmationEmail(order.getUser().getEmail(), order.getIdentifier(), orderDTO.getTotalWithShipping());
    }

    @Override
    public OrderDTO getOrderInfo(String orderIdentifier) {
        if (!isValidUUID(orderIdentifier)) {
            throw new OrderException(String.format("Invalid order identifier '%s'", orderIdentifier), HttpStatus.BAD_REQUEST);
        }

        Order order = orderRepository.findByIdentifier(UUID.fromString(orderIdentifier))
                .orElseThrow(() -> new OrderException(String.format("Invalid order identifier '%s'", orderIdentifier), HttpStatus.BAD_REQUEST));

        return convertOrderIntoOrderDTO(order);
    }

    @Override
    public List<OrderDTO> findByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException("Server Error", HttpStatus.INTERNAL_SERVER_ERROR));

        List<Order> orders = orderRepository.findByUser(user);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order : orders) {
            OrderDTO temp = convertOrderIntoOrderDTO(order);
            orderDTOS.add(temp);
        }

        return orderDTOS;
    }

    @Override
    public List<OrderDTO> findByEmail(String email) {
        List<Order> orders = orderRepository.findByShippingInfoEmail(email);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order : orders) {
            OrderDTO temp = convertOrderIntoOrderDTO(order);
            orderDTOS.add(temp);
        }

        return orderDTOS;
    }

    public boolean isValidUUID(String uuid) {
        return uuid != null && UUID_PATTERN.matcher(uuid).matches();
    }

    public OrderDTO convertOrderIntoOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        if (order.getUser() != null) {
            orderDTO.setUser(order.getUser().getEmail());
        }
        orderDTO.setIdentifier(order.getIdentifier());

        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                OrderItemDTO orderItemDTO = new OrderItemDTO();
                orderItemDTO.setProductId(item.getProductDetail().getId());
                orderItemDTO.setTitle(item.getProductDetail().getProduct().getTitle());
                orderItemDTO.setSlug(item.getProductDetail().getProduct().getSlug());

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
                orderItemDTO.setImage(imageUrl);

                orderItemDTO.setColor(item.getProductDetail().getColor().getColorName());
                orderItemDTO.setColorCode(item.getProductDetail().getColor().getColorHexCode());
                orderItemDTO.setSize(item.getProductDetail().getSize().getSize());
                orderItemDTO.setInventory(item.getProductDetail().getInventory());
                orderItemDTO.setPrice(item.getPrice());
                orderItemDTO.setSelectedQuantity(item.getQuantity());
                orderItemDTOS.add(orderItemDTO);
            }

            BigDecimal totalWithoutShipping = BigDecimal.ZERO;
            for (OrderItemDTO orderItemDTO : orderItemDTOS) {
                BigDecimal itemTotal = orderItemDTO.getPrice().multiply(BigDecimal.valueOf(orderItemDTO.getSelectedQuantity()));
                totalWithoutShipping = totalWithoutShipping.add(itemTotal);
            }
            totalWithoutShipping = totalWithoutShipping.setScale(2, RoundingMode.HALF_UP);
            orderDTO.setTotalWithoutShipping(totalWithoutShipping);

            BigDecimal threshold = new BigDecimal("30.00");
            BigDecimal shippingCost = new BigDecimal("7.99");
            if (totalWithoutShipping.compareTo(threshold) < 0) {
                orderDTO.setShippingPrice(shippingCost);
                orderDTO.setTotalWithShipping(totalWithoutShipping.add(shippingCost));
            } else {
                orderDTO.setShippingPrice(BigDecimal.ZERO);
                orderDTO.setTotalWithShipping(totalWithoutShipping);
            }

            orderDTO.setOrderItems(orderItemDTOS);
            orderDTO.setShippingInfo(order.getShippingInfo());
        }

        return orderDTO;
    }

}
