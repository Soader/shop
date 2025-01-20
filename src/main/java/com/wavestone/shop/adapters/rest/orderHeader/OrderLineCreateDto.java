package com.wavestone.shop.adapters.rest.orderHeader;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderLineCreateDto {

    private String description;
    private Integer quantity;
    private Long productId;
}
