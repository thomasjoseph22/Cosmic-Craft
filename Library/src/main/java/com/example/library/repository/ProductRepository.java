package com.example.library.repository;

import com.example.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findById(long id);

    @Query(value = "select * from products where is_activated = true and category_id = :id", nativeQuery = true)
    List<Product> findAllByActivatedTrue(@Param("id") long id);

    @Query(value = "select * from products where is_activated = true", nativeQuery = true)
    List<Product> findAllByActivatedTrue();

    @Query(value = "select * from products ORDER BY product_id DESC",nativeQuery = true)
    List<Product> findAllByOrderById();

    @Query(value = "select count(*) from Product")
    Long CountAllProducts();

    @Query(value = "SELECT p.product_id, p.name, c.name, " +
            "SUM(od.quantity) AS total_quantity_ordered, SUM(od.quantity * p.cost_Price) AS total_revenue " +
            "FROM products p " +
            "JOIN order_Detail od ON p.product_id = od.product_id " +
            "JOIN orders o ON od.order_id = o.order_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE o.order_Status = 'Delivered' " +
            "GROUP BY p.product_id, p.name, c.name " +
            "ORDER BY total_revenue DESC",nativeQuery = true)
    List<Object[]> getProductStatsForConfirmedOrders();

    @Query(value = "SELECT p.product_id, p.name, c.name, " +
            "SUM(od.quantity) AS total_quantity_ordered, SUM(od.quantity * p.cost_Price) AS total_revenue " +
            "FROM products p " +
            "JOIN order_Detail od ON p.product_id = od.product_id " +
            "JOIN orders o ON od.order_id = o.order_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE o.order_Status = 'Delivered' " +
            "AND o.order_date BETWEEN :startDate AND :endDate " +
            "GROUP BY p.product_id, p.name, c.name " +
            "ORDER BY total_revenue DESC",nativeQuery = true)
    List<Object[]> getProductsStatsForConfirmedOrdersBetweenDates(@Param("startDate") Date startDate,@Param("endDate") Date endDate);


    List<Product> findAllByCategoryId(long id);



    @Query(value = "SELECT * FROM products WHERE is_activated = true ORDER BY CASE WHEN :sort = 'lowToHigh' THEN cost_price END ASC, CASE WHEN :sort = 'highToLow' THEN cost_price END DESC", nativeQuery = true)
    List<Product> findAllByActivatedTrueAndSortBy(@Param("sort") String sort);



    List<Product> findAllByNameContainingIgnoreCase(String keyword);
}
