package com.wavestone.shop.order.application;

import com.wavestone.shop.adapters.rest.orderHeader.OrderHeaderCreateDto;
import com.wavestone.shop.adapters.rest.orderHeader.OrderHeaderResponseDto;
import com.wavestone.shop.adapters.rest.orderLine.OrderLineDto;
import com.wavestone.shop.domain.OrderHeader;
import com.wavestone.shop.domain.OrderHeaderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderHeaderMapper {

    public OrderHeader toEntity(OrderHeaderCreateDto dto) {
        return OrderHeader.builder()
                .description(dto.getDescription())
                .orderDate(LocalDateTime.now())
                .status(OrderHeaderStatus.CREATED)
                .build();
    }

    public OrderHeaderResponseDto toResponseDto(OrderHeader orderHeader){
        OrderHeaderResponseDto responseDto = OrderHeaderResponseDto.builder()
                .orderId(orderHeader.getId())
                .status(orderHeader.getStatus())
                .orderDate(orderHeader.getOrderDate())
                .customerEmail(orderHeader.getCustomer().getEmail())
                .build();

        List<OrderLineDto> orderLinesDto = orderHeader.getOrderLines().stream().map(line -> OrderLineDto.builder()
                .orderLineId(line.getId())
                .quantity(line.getQuantity())
                .productName(line.getProduct().getName())
                .build()).toList();

        responseDto.setOrderLines(orderLinesDto);
        return responseDto;
    }
}