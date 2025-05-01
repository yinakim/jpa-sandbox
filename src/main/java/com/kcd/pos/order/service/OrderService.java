package com.kcd.pos.order.service;

import com.kcd.pos.common.constants.DataStatus;
import com.kcd.pos.common.constants.DiscountType;
import com.kcd.pos.order.domain.Discount;
import com.kcd.pos.order.domain.OrderItem;
import com.kcd.pos.order.domain.OrderItemOption;
import com.kcd.pos.order.domain.Orders;
import com.kcd.pos.order.dto.OrderItemOptionRegisterReq;
import com.kcd.pos.order.dto.OrderItemRegisterReq;
import com.kcd.pos.order.dto.OrderRegisterReq;
import com.kcd.pos.order.repository.OrderItemOptionRepository;
import com.kcd.pos.order.repository.OrderItemRepository;
import com.kcd.pos.order.repository.OrderRepository;
import com.kcd.pos.product.domain.Option;
import com.kcd.pos.product.domain.Product;
import com.kcd.pos.product.repository.OptionGroupRepository;
import com.kcd.pos.product.repository.OptionRepsitory;
import com.kcd.pos.product.repository.ProductOptionGroupRepository;
import com.kcd.pos.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
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
        }

        // 2. Order 생성
        Orders order = Orders.builder()
                .originPrice(request.getOriginPrice())
                .totalPrice(request.getTotalPrice())
                .discountPrice(request.getDiscountPrice())
                .discount(discount) // todo. nullable
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

                orderItem.addOrderItemOption(orderItemOption); // list에 itemOption 하나씩 add & 자식과 부모 연결 (Option -> OrderItem)
            } // orderItemOptions

            // orderItem 하나씩 추가 & 자식과 부모 연결 (OrderItem -> Order)
            order.addOrderItem(orderItem);
        } // orderItems

        // order 저장 -> cascade에 의해 자식들 모두 저장됨
        Orders savedOrder = orderRepository.save(order);
        System.out.println("**************************************************** 주문ID : "+savedOrder.getOrderId());
    }



    /**
     * 주문등록
     * 1. 할인여부 체크(nullable이므로)
     * 2. Order 생성
     * 3. 주문항목처리
     * 3-1. 옵션처리
     * 4. 저장
     */
    @Transactional
    public void registerOrder_(OrderRegisterReq request) {

        // 1. 할인 체크, 변환
        Discount discount = null;
        if(Objects.nonNull(request.getDiscount())) {
            discount = Discount.builder()
                    .discountType(DiscountType.valueOf(request.getDiscount().getDiscountType()))
                    .discountValue(request.getDiscount().getDiscountValue())
                    .build();
        }

        // 2. Order 생성, 저장 : orderId 생성
        Orders order = Orders.builder()
                .originPrice(request.getOriginPrice())
                .totalPrice(request.getTotalPrice())
                .discountPrice(request.getDiscountPrice())
                .discount(discount) // todo. nullable
                .deleteYn(DataStatus.DELETE_N)
                .build();
        Orders savedOrder = orderRepository.save(order);


        // 3. 주문항목 처리
        for (OrderItemRegisterReq itemReq : request.getOrderItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품입니다."));

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getProductId())
                    .productNm(product.getProductNm())
                    .itemPrice(product.getPrice())
                    .itemQuantity(itemReq.getItemQuantity())
                    .order(savedOrder) // DB에 먼저 저장한 Orders를 넣음, orderId 있음
                    .build();

            // 옵션이 있는 경우만 처리
            List<OrderItemOption> orderItemOptions = new ArrayList<>();
            for (OrderItemOptionRegisterReq itemOptionReq : itemReq.getItemOptions()) {
                Option option = optionRepsitory.findByOptionIdAndDeleteYn(itemOptionReq.getOptionId(), DataStatus.DELETE_N)
                        .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

                OrderItemOption orderItemOption = OrderItemOption.builder()
                        .optionId(option.getOptionId())
                        .optionNm(option.getOptionNm())
                        .extraPrice(option.getExtraPrice())
                        .orderItem(orderItem) // 아직 OrderItem은 OrderItemId 없음
                        .build();

                orderItemOptionRepository.save(orderItemOption); //  연관관계 저장하기 위해 최하위 orderItemOption 먼저 저장
            }// OrderItemOption 처리

            orderItemRepository.save(orderItem); // orderItem 저장
        } // orderItems 처리

        System.out.println("**************************************************** 주문ID : "+savedOrder.getOrderId());
    }
}
