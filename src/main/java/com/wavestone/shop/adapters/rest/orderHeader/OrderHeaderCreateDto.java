package com.wavestone.shop.adapters.rest.orderHeader;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderHeaderCreateDto {

    private String description;
    private Long customerId;
    private List<OrderLineCreateDto> orderLines;
}
