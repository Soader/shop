package com.wavestone.shop.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {

    @Query("""
            SELECT o FROM OrderHeader o
            JOIN FETCH o.orderLines
            WHERE o.customer.email =:customerEmail
            """)
    List<OrderHeader> findByCustomerEmail(@Param("customerEmail") String customerEmail);

    @Query("""
            SELECT DISTINCT oh FROM OrderHeader oh
            JOIN FETCH oh.orderLines ol
            JOIN FETCH ol.product p
            WHERE p.name =:productName
            """)
    List<OrderHeader> findByProductName(String productName);

    @Query("""
            SELECT o FROM OrderHeader o
            JOIN FETCH o.orderLines
            WHERE o.status =:status
            """)
    Page<OrderHeader> findByStatus(@Param("status") OrderHeaderStatus status, Pageable pageable);

    @Query("""
            SELECT o FROM OrderHeader o
            JOIN FETCH o.customer c
            JOIN FETCH o.orderLines l
            JOIN FETCH l.product p
            WHERE (
                LOWER(c.email) LIKE %:search% OR
                LOWER(p.name) LIKE %:search% OR
                LOWER(o.status) LIKE %:search% OR
                CAST(o.id AS string) LIKE %:search%
            )
            """)
    Page<OrderHeader> findBySearch(
            @Param("search") String search,
            Pageable pageable);

    @Query("""
        SELECT o FROM OrderHeader o
        JOIN FETCH o.customer c
        JOIN FETCH o.orderLines l
        JOIN FETCH l.product p
        WHERE (:globalSearch IS NULL OR (
            LOWER(c.email) LIKE LOWER(CONCAT('%', :globalSearch, '%')) OR
            LOWER(p.name) LIKE LOWER(CONCAT('%', :globalSearch, '%')) OR
            LOWER(o.status) LIKE LOWER(CONCAT('%', :globalSearch, '%')) OR
            CAST(o.id AS string) LIKE LOWER(CONCAT('%', :globalSearch, '%'))
        ))
        AND (:orderId IS NULL OR o.id = :orderId)
        AND (:status IS NULL OR LOWER(o.status) LIKE LOWER(CONCAT('%', :status, '%')))
        AND (:customerEmail IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :customerEmail, '%')))
        AND (:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')))
    """)
    Page<OrderHeader> findFilteredOrders(
            @Param("globalSearch") String globalSearch,
            @Param("orderId") String orderId,
            @Param("status") String status,
            @Param("customerEmail") String customerEmail,
            @Param("productName") String productName,
            Pageable pageable
    );

    Optional<OrderHeader> findByIdempotencyKey(String idempotencyKey);
}
