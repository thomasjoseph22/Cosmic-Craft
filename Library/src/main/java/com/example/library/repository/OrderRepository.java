package com.example.library.repository;

import com.example.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query(value = "select * from orders where customer_id = :id order by order_id desc ",nativeQuery = true)
    List<Order> findAllBy(@Param("id")long id);



    Order findById(long id);
}
