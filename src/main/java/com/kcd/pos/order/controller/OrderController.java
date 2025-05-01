package com.kcd.pos.order.controller;

import com.kcd.pos.order.dto.OrderRegisterReq;
import com.kcd.pos.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pos/order/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<Void> registerOrder(@RequestBody OrderRegisterReq orderRegisterReq){
        service.registerOrder(orderRegisterReq);
        return ResponseEntity.ok().build();
    }
}
