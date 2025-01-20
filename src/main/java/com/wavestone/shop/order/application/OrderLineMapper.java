package com.wavestone.shop.order.application;

import com.wavestone.shop.adapters.rest.orderHeader.OrderLineCreateDto;
import com.wavestone.shop.domain.OrderLine;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper {

    public OrderLine toEntity(OrderLineCreateDto dto) {
        return OrderLine.builder()
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .build();
    }

    public OrderLineCreateDto toDto(OrderLine orderLine) {
        OrderLineCreateDto dto = new OrderLineCreateDto();
        dto.setDescription(orderLine.getDescription());
        dto.setQuantity(orderLine.getQuantity());
        dto.setProductId(orderLine.getProduct().getId());
        return dto;
    }
}

