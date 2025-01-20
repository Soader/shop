package com.wavestone.shop.adapters.rest.orderHeader;

import com.wavestone.shop.domain.OrderHeaderStatus;
import com.wavestone.shop.order.application.OrderHeaderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class OrderHeaderController {
    private final OrderHeaderService orderHeaderService;

    @PostMapping(path = "/order-header")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOrderHeader(@RequestBody OrderHeaderCreateDto dto, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return orderHeaderService.createOrder(dto, idempotencyKey);
    }

    @GetMapping(path = "/order-header")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderHeaderResponseDto> findBySearch(
            @RequestParam(required = false) String globalSearch,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false) String productName,
            Pageable pageable
    ) {
        return orderHeaderService.findBySearch(globalSearch, orderId, status, customerEmail, productName, pageable);
    }

    @GetMapping(path = "/order-header/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderHeaderResponseDto> findByStatus(
            @PathVariable OrderHeaderStatus status, Pageable pageable) {
        return orderHeaderService.findByStatus(status, pageable);
    }

    @GetMapping(path = "/order-header/id/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderHeaderResponseDto findById(@PathVariable Long orderId){
        return orderHeaderService.findById(orderId);
    }

    @GetMapping(path = "/order-header/email/{customerEmail}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderHeaderResponseDto> findByCustomerEmail(@PathVariable String customerEmail){
        return orderHeaderService.findByCustomerEmail(customerEmail);
    }

    @GetMapping(path = "/order-header/product-name/{productName}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderHeaderResponseDto> findByProductName(@PathVariable String productName){
        return orderHeaderService.findByProductName(productName);
    }
}