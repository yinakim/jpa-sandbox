package com.kcd.pos.order.controller;

import com.kcd.pos.order.dto.*;
import com.kcd.pos.order.service.OrderMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "주문관리 API")
@RestController
@RequestMapping("/pos/order/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMasterService service;

    @Operation(summary = "신규 주문 등록", description = "신규 주문(주문항목 별 주문옵션 포함)을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "신규주문 등록 성공")
    @PostMapping
    public ResponseEntity<OrderRegisterRes> registerOrder(@RequestBody OrderRegisterReq orderRegisterReq){
        OrderRegisterRes orderRegisterRes = service.registerOrder(orderRegisterReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderRegisterRes);
    }

    @Operation(summary = "주문 목록 조회", description = "")
    @ApiResponse(responseCode = "200", description = "주문목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<OrderRes>> getOrders(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ){
        OrderReq request = OrderReq.fromOrderReqParam(orderId, storeId, fromDate, toDate);
        List<OrderRes> orderRes = service.getOrders(request);
        return ResponseEntity.status(HttpStatus.OK).body(orderRes);
    }

    @Operation(summary = "주문 단건 상세조회", description = "")
    @ApiResponse(responseCode = "200", description = "주문 조회 성공")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRes> getOrderDetail(@PathVariable Long orderId){
        OrderRes orderRes = service.getOrderDetail(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderRes);
    }

    @Operation(summary = "주문 항목 수정 - 수량변경", description = "")
    @ApiResponse(responseCode = "204", description = "주문항목 수정 성공")
    @PatchMapping("/item/{orderItemId}")
    public ResponseEntity<Void> updateItemQuantity(@PathVariable Long orderItemId, @RequestBody OrderItemQuantiyReq request){
        service.updateItemQuantity(orderItemId, request.getItemQuantity());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "주문 항목 삭제", description = "")
    @ApiResponse(responseCode = "204", description = "주문 항목 삭제 성공")
    @DeleteMapping("/item/{orderItemId}")
    public ResponseEntity<Void> safeDeleteOrderItem(@PathVariable Long orderItemId){
        service.safeDeleteOrderItem(orderItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "주문 단건 삭제", description = "")
    @ApiResponse(responseCode = "204", description = "주문 삭제 성공")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> safeDeleteOrder(@PathVariable Long orderId){
        service.safeDeleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
