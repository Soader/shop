package com.wavestone.shop.order.application;

import com.wavestone.shop.adapters.rest.orderHeader.OrderHeaderCreateDto;
import com.wavestone.shop.adapters.rest.orderHeader.OrderHeaderResponseDto;
import com.wavestone.shop.adapters.rest.orderHeader.OrderLineCreateDto;
import com.wavestone.shop.adapters.rest.orderLine.OrderLineDto;
import com.wavestone.shop.domain.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderHeaderService {

    private final OrderHeaderRepository orderHeaderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderHeaderMapper orderHeaderMapper;
    private final OrderLineMapper orderLineMapper;

    @Transactional
    public Long createOrder(OrderHeaderCreateDto orderHeaderCreateDto, String idempotencyKey) {
        Optional<OrderHeader> orderFound = orderHeaderRepository.findByIdempotencyKey(idempotencyKey);
        if (orderFound.isPresent()) {
            return orderFound.get().getId();
        }

        Customer customer = customerRepository.findById(orderHeaderCreateDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + orderHeaderCreateDto.getCustomerId() + " does not exist."));
        Map<Long, Product> productMap = getIdProductMap(orderHeaderCreateDto);

        OrderHeader orderHeader = orderHeaderMapper.toEntity(orderHeaderCreateDto);
        orderHeader.setCustomer(customer);
        orderHeader.setIdempotencyKey(idempotencyKey);

        orderHeaderCreateDto.getOrderLines().forEach(line -> {
            OrderLine orderLine = orderLineMapper.toEntity(line);
            Product product = productMap.get(line.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product with ID " + line.getProductId() + " is not found.");
            }
            orderLine.setProduct(product);
            orderHeader.addLine(orderLine);
        });
        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);
        return savedOrder.getId();
    }

    private Map<Long, Product> getIdProductMap(OrderHeaderCreateDto orderHeaderCreateDto) {
        return orderHeaderCreateDto.getOrderLines().stream()
                .map(OrderLineCreateDto::getProductId)
                .distinct()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    @Transactional(readOnly = true)
    public OrderHeaderResponseDto findById(Long orderId) {
        OrderHeader orderHeader = orderHeaderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("OrderHeader with ID: " + orderId + " does not exist."));
        return orderHeaderMapper.toResponseDto(orderHeader);
    }

    @Transactional(readOnly = true)
    public List<OrderHeaderResponseDto> findByCustomerEmail(String customerEmail) {
        List<OrderHeader> ordersByCustomerEmail = orderHeaderRepository.findByCustomerEmail(customerEmail);
        return ordersByCustomerEmail.stream().map(orderHeaderMapper::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderHeaderResponseDto> findByProductName(String productName) {
        List<OrderHeader> ordersByProductName = orderHeaderRepository.findByProductName(productName);
        return ordersByProductName.stream().map(orderHeaderMapper::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public Page<OrderHeaderResponseDto> findByStatus(OrderHeaderStatus status, Pageable pageable) {
        return orderHeaderRepository.findByStatus(status, pageable)
                .map(orderHeaderMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<OrderHeaderResponseDto> findBySearch(
            String globalSearch,
            String orderId,
            String status,
            String customerEmail,
            String productName,
            Pageable pageable)
    {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("orderDate").descending());
        }
        return orderHeaderRepository.findFilteredOrders(globalSearch, orderId, status, customerEmail, productName, pageable)
                .map(orderHeaderMapper::toResponseDto);
    }
}
