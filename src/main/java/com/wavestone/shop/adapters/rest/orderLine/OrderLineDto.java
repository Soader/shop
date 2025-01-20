package com.wavestone.shop.adapters.rest.orderLine;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class OrderLineDto {

    private Long orderLineId;
    private Integer quantity;
    private String productName;
}
