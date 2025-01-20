package com.wavestone.shop.adapters.rest.orderHeader;

import com.wavestone.shop.adapters.rest.orderLine.OrderLineDto;
import com.wavestone.shop.domain.OrderHeaderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class OrderHeaderResponseDto {

    private Long orderId;
    private LocalDateTime orderDate;
    private OrderHeaderStatus status;
    private String customerEmail;
    private List<OrderLineDto> orderLines;

}
