package com.kcd.pos.order.service;

import com.kcd.pos.common.constants.DataStatus;
import com.kcd.pos.common.constants.DiscountType;
import com.kcd.pos.order.domain.Discount;
import com.kcd.pos.order.domain.OrderItem;
import com.kcd.pos.order.domain.OrderItemOption;
import com.kcd.pos.order.domain.OrderMaster;
import com.kcd.pos.order.dto.*;
import com.kcd.pos.order.repository.OrderItemOptionRepository;
import com.kcd.pos.order.repository.OrderItemRepository;
import com.kcd.pos.order.repository.OrderMasterRepository;
import com.kcd.pos.product.domain.Option;
import com.kcd.pos.product.domain.Product;
import com.kcd.pos.product.repository.OptionGroupRepository;
import com.kcd.pos.product.repository.OptionRepsitory;
import com.kcd.pos.product.repository.ProductOptionGroupRepository;
import com.kcd.pos.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMasterService {
    private final OrderMasterRepository orderMasterRepository;
    private final ProductRepository productRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final ProductOptionGroupRepository productOptionGroupRepository;
    private final OptionRepsitory optionRepsitory;
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 주문등록
     * 1. 할인여부 체크(nullable이므로)
     * 2. Order 생성
     * 3. 주문항목처리
     * 3-1. 옵션처리
     * 4. 저장
     */
    @Transactional
    public void registerOrder(OrderRegisterReq request) {

        // 1. 할인 체크, 변환
        Discount discount = null;
        if(Objects.nonNull(request.getDiscount())) {
            discount = Discount.builder()
                    .discountType(DiscountType.valueOf(request.getDiscount().getDiscountType()))
                    .discountValue(request.getDiscount().getDiscountValue())
                    .build();
        } else {
            discount = new Discount();
        }

        // 2. Order 생성
        OrderMaster orderMaster = OrderMaster.builder()
                .originPrice(request.getOriginPrice())
                .totalPrice(request.getTotalPrice())
                .discountPrice(request.getDiscountPrice())
                .discount(discount)
                .deleteYn(DataStatus.DELETE_N)
                .build();

        // 3. 주문항목 처리
        for (OrderItemRegisterReq itemReq : request.getOrderItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품입니다."));

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getProductId())
                    .productNm(product.getProductNm())
                    .itemPrice(product.getPrice())
                    .deleteYn(DataStatus.DELETE_N)
                    .itemQuantity(itemReq.getItemQuantity())
                    .build();

            // 옵션이 있는 경우만 처리

            for (OrderItemOptionRegisterReq itemOptionReq : itemReq.getItemOptions()) {
                Option option = optionRepsitory.findByOptionIdAndDeleteYn(itemOptionReq.getOptionId(), DataStatus.DELETE_N)
                        .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

                OrderItemOption orderItemOption = OrderItemOption.builder()
                        .optionId(option.getOptionId())
                        .optionNm(option.getOptionNm())
                        .extraPrice(option.getExtraPrice())
                        .orderItem(orderItem) // 아직 OrderItem.OrderItemId 없음
                        .build();

                orderItem.addOrderItemOption(orderItemOption);
            } // orderItemOptions

            orderMaster.addOrderItem(orderItem);
        } // orderItems

        OrderMaster savedOrder = orderMasterRepository.save(orderMaster);
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
                //request.getDeleteYn()
        );

        List<OrderRes> responseList = new java.util.ArrayList<>();

        for (OrderMaster order : orders) {
            List<OrderItemRes> itemResList = new java.util.ArrayList<>();

            for (OrderItem item : order.getOrderItems()) {
                List<OrderItemOptionRes> optionResList = new java.util.ArrayList<>();

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

    // 할인정보 엔티티 -> dto
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
        if(Objects.isNull(orderId)) {
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
     * 주문항목 수량변경
     * @param orderItemId
     */
    @Transactional
    public void updateItemQuantity(Long orderItemId, int newQuantity) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        orderItem.changeItemQuantity(newQuantity);
    }
}



