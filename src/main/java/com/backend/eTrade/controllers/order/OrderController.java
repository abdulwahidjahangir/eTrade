package com.backend.eTrade.controllers.order;

import com.backend.eTrade.security.service.UserDetailsImpl;
import com.backend.eTrade.services.order.OrderService;
import com.backend.eTrade.services.order.dtos.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/public/get-order/{Order-Identifier}")
    public ResponseEntity<?> getOrder(
            @PathVariable("Order-Identifier") String orderIdentifier
    ) {
        OrderDTO orderDTO = orderService.getOrderInfo(orderIdentifier);

        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/admin/get-my-orders")
    public ResponseEntity<?> getMyOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String userEmail = userDetails != null ? userDetails.getEmail() : null;
        List<OrderDTO> orderDTOS = orderService.findByUser(userEmail);

        return ResponseEntity.ok(orderDTOS);
    }

    @GetMapping("/public/get-my-orders")
    public ResponseEntity<?> getMyOrderByEmail(
            @RequestParam("email") String email
    ) {
        List<OrderDTO> orderDTOS = orderService.findByEmail(email);

        return ResponseEntity.ok(orderDTOS);
    }

}
