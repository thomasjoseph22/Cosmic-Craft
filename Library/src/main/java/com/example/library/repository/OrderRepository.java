package com.example.library.repository;

import com.example.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query(value = "select * from orders where customer_id = :id order by order_id desc ",nativeQuery = true)
    List<Order> findAllBy(@Param("id")long id);

    @Query(value = "select count(*) from orders where order_date between :startDate and :endDate and order_status = :orderStatus",nativeQuery = true)
    Long countByOrderDateBetweenAndOrderStatus(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("orderStatus") String orderStatus);


    @Query(value = "select COALESCE(SUM(o.totalPrice),0) FROM Order o where o.orderStatus = 'Delivered'")
    Double getTotalConfirmedOrdersAmount();

    @Query(value = "select COUNT(*) FROM Order o WHERE o.orderStatus='Delivered'")
    Long countAllConfirmedOrders();

    @Query(value = "select COALESCE(SUM(total_price), 0) from orders where order_date between :startDate and :endDate and order_status = :orderStatus",nativeQuery = true)
    Double getTotalConfirmedOrdersAmountForMonth(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("orderStatus") String orderStatus);

    Order findById(long id);
}
