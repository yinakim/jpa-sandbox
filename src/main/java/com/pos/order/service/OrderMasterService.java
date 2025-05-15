package com.pos.order.service;

import com.pos.common.constants.DataStatus;
import com.pos.common.constants.DiscountType;
import com.pos.common.constants.TestConstants;
import com.pos.order.domain.Discount;
import com.pos.order.domain.OrderItem;
import com.pos.order.domain.OrderItemOption;
import com.pos.order.domain.OrderMaster;
import com.pos.order.dto.*;
import com.pos.order.repository.OrderItemRepository;
import com.pos.order.repository.OrderMasterRepository;
import com.pos.product.domain.Option;
import com.pos.product.domain.Product;
import com.pos.product.repository.OptionRepsitory;
import com.pos.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderMasterService {
    private final OrderMasterRepository orderMasterRepository;
    private final ProductRepository productRepository;
    private final OptionRepsitory optionRepsitory;
    private final OrderItemRepository orderItemRepository;

    /**
     * 주문등록
     * 1. 할인여부 체크(null인경우 EMPTY처리)
     * 2. Order 생성
     * 3. 주문항목처리
     * 3-1. 옵션처리
     * 4. 저장
     */
    @Transactional
    public OrderRegisterRes registerOrder(OrderRegisterReq request) {
        int originPriceSum = 0; // 주문 총액 합산
        int itemPriceSum = 0;   // 상품가격 합산
        int extraPriceSum = 0;  // 옵션 추가금 합산

        // 2. Order 생성
        OrderMaster orderMaster = OrderMaster.builder()
                .discount(request.toDiscount()) // 할인 체크, 변환
                .deleteYn(DataStatus.DELETE_N)
                .build();

        // 3. 주문항목 처리
        for (OrderItemRegisterReq itemReq : request.getOrderItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품입니다."));
            int productPrice = product.getPrice();
            Integer itemQuantity = itemReq.getItemQuantity();
            int itemPrice = productPrice * itemQuantity;

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getProductId())
                    .productNm(product.getProductNm())
                    .itemPrice(productPrice)
                    .itemQuantity(itemQuantity)
                    .deleteYn(DataStatus.DELETE_N)
                    .build();

            // 옵션이 있는 경우만 처리
            for (OrderItemOptionRegisterReq itemOptionReq : itemReq.getItemOptions()) {
                Option option = optionRepsitory.findByOptionIdAndDeleteYn(itemOptionReq.getOptionId(), DataStatus.DELETE_N)
                        .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));
                int extraPrice = option.getExtraPrice();

                OrderItemOption orderItemOption = OrderItemOption.builder()
                        .optionId(option.getOptionId())
                        .optionNm(option.getOptionNm())
                        .extraPrice(extraPrice)
                        .orderItem(orderItem) // 아직 OrderItem.OrderItemId 없음
                        .build();
                extraPriceSum += extraPrice; // itemOption별 상품가격 누적
                orderItem.addOrderItemOption(orderItemOption);
            } // orderItemOptions

            itemPriceSum += itemPrice; // item 별 상품가격 누적
            orderMaster.addOrderItem(orderItem);
        } // orderItems

        originPriceSum = itemPriceSum + extraPriceSum;
        orderMaster.calculateOrderPrices(originPriceSum);
        OrderMaster savedOrder = orderMasterRepository.save(orderMaster);
        return makeBill(savedOrder);
    }

    /**
     * 주문결과 세팅
     */
    private OrderRegisterRes makeBill(OrderMaster savedOrder) {
        // 할인내용
        Discount discount = savedOrder.getDiscount();
        DiscountType discountType = discount.getDiscountType();
        int discountValue = discount.getDiscountValue();

        // 항목 & 항목 별 옵션 세팅
        List<OrderItemRes> itemResList = savedOrder.getOrderItems().stream()
                .map(orderItem -> {
                    List<OrderItemOptionRes> optionResList = orderItem.getOrderItemOptions().stream()
                            .map(option -> OrderItemOptionRes.builder()
                                    .orderItemOptionId(option.getOrderItemOptionId())
                                    .optionId(option.getOptionId())
                                    .optionNm(option.getOptionNm())
                                    .extraPrice(option.getExtraPrice())
                                    .build())
                            .collect(Collectors.toList());

                    return OrderItemRes.builder()
                            .orderItemId(orderItem.getOrderItemId())
                            .itemPrice(orderItem.getItemPrice())
                            .itemQuantity(orderItem.getItemQuantity())
                            .productId(orderItem.getProductId())
                            .productNm(orderItem.getProductNm())
                            .options(optionResList)
                            .build();
                }).collect(Collectors.toList());


        return OrderRegisterRes.builder()
                .orderId(savedOrder.getOrderId())
                .storeId(TestConstants.SAMPLE_STORE_ID) // 받아온 상점 ID사용
                .posId(TestConstants.SAMPLE_POS_ID) // 받아온 POS ID 사용
                .originPrice(savedOrder.getOriginPrice())
                .discountPrice(savedOrder.getDiscountPrice())
                .totalPrice(savedOrder.getTotalPrice())
                .orderCreatedAt(savedOrder.getCreatedAt())
                .orderCreatedBy(savedOrder.getCreatedBy())
                .orderItems(itemResList)
                .discountType(discountType.getTypeName())
                .discountPercent(discountType.equals(DiscountType.PERCENT) ? discountValue : 0)
                .discountAmount(discountType.equals(DiscountType.AMOUNT) ? discountValue : 0)
                .build();
    }


    /**
     * 주문 목록 조회
     */
    public List<OrderRes> getOrders(OrderReq request) {
        // 조건 기반 조회
        List<OrderMaster> orders = orderMasterRepository.findAllByConditions(
                request.getOrderId(),
                request.getFromDate(),
                request.getToDate()
        );

        List<OrderRes> responseList = new ArrayList<>();
        for (OrderMaster order : orders) {
            List<OrderItemRes> itemResList = new ArrayList<>();

            for (OrderItem item : order.getOrderItems()) {
                List<OrderItemOptionRes> optionResList = new ArrayList<>();

                for (OrderItemOption option : item.getOrderItemOptions()) {
                    OrderItemOptionRes optionRes = OrderItemOptionRes.builder()
                            .orderItemOptionId(option.getOrderItemOptionId())
                            .optionId(option.getOptionId())
                            .optionNm(option.getOptionNm())
                            .extraPrice(option.getExtraPrice())
                            .build();
                    optionResList.add(optionRes);
                }

                OrderItemRes itemRes = OrderItemRes.builder()
                        .orderItemId(item.getOrderItemId())
                        .productId(item.getProductId())
                        .productNm(item.getProductNm())
                        .itemPrice(item.getItemPrice())
                        .itemQuantity(item.getItemQuantity())
                        .options(optionResList)
                        .build();
                itemResList.add(itemRes);
            }

            DiscountRes discountRes = discountResMapper(order.getDiscount());
            OrderRes orderRes = OrderRes.builder()
                    .orderId(order.getOrderId())
                    .originPrice(order.getOriginPrice())
                    .discountPrice(order.getDiscountPrice())
                    .totalPrice(order.getTotalPrice())
                    .discount(discountRes)
                    .orderItems(itemResList)
                    .build();
            responseList.add(orderRes);
        }
        return responseList;
    }

    /**
     * DiscountRes - Discount 매핑
     */
    private DiscountRes discountResMapper(Discount discount) {
        return DiscountRes.builder()
                .discountType(discount.getDiscountType().name())
                .discountValue(discount.getDiscountValue())
                .discountPercent(discount.getDiscountPercent())
                .discountAmount(discount.getDiscountAmount())
                .build();
    }

    /**
     * 주문 상세조회
     */
    public OrderRes getOrderDetail(Long orderId) {
        if (Objects.isNull(orderId)) {
            throw new IllegalArgumentException("주문상세조회 시, orderId는 필수입니다.");
        }

        OrderMaster order = orderMasterRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 주문ID 입니다. 주문ID:" + orderId));

        return OrderRes.builder()
                .orderId(order.getOrderId())
                .originPrice(order.getOriginPrice())
                .discountPrice(order.getDiscountPrice())
                .totalPrice(order.getTotalPrice())
                .discount(discountResMapper(order.getDiscount()))
                .orderItems(order.getOrderItems().stream()
                        .map(item -> OrderItemRes.builder()
                                .orderItemId(item.getOrderItemId())
                                .productId(item.getProductId())
                                .productNm(item.getProductNm())
                                .itemPrice(item.getItemPrice())
                                .itemQuantity(item.getItemQuantity())
                                .options(item.getOrderItemOptions().stream()
                                        .map(opt -> OrderItemOptionRes.builder()
                                                .orderItemOptionId(opt.getOrderItemOptionId())
                                                .optionId(opt.getOptionId())
                                                .optionNm(opt.getOptionNm())
                                                .extraPrice(opt.getExtraPrice())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 주문 삭제 - safeDelete
     */
    @Transactional
    public void safeDeleteOrder(Long orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문건입니다."));

        // 1. order item safeDelete
        orderMaster.getOrderItems()
                .forEach(OrderItem::safeDelete);

        // 2. order safeDelete
        orderMaster.safeDelete();
    }

    /**
     * 주문 항목 삭제 - 휴지통버튼
     */
    @Transactional
    public void safeDeleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문항목입니다."));
        // 마지막 항목만 남았는데 삭제하는 경우
        if (1 == orderItem.getOrderMaster().getOrderItems().size()) {
            orderItem.getOrderMaster().safeDelete(); // order safeDelete
        }
        orderItem.safeDelete(); // order item safeDelete
    }

    /**
     * 주문항목 수량변경
     *
     * @param orderItemId
     */
    @Transactional
    public void updateItemQuantity(Long orderItemId, int newQuantity) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        orderItem.changeItemQuantity(newQuantity);
    }
}



