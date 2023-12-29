package com.example.library.service.impl;

import com.example.library.model.*;
import com.example.library.repository.OrderDetailRepository;
import com.example.library.repository.OrderRepository;
import com.example.library.repository.ProductRepository;
import com.example.library.service.AddressService;
import com.example.library.service.OrderService;
import com.example.library.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    private ShoppingCartService shoppingCartService;

    private ProductRepository productRepository;

    private AddressService addressService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ShoppingCartService shoppingCartService,
                            ProductRepository productRepository,AddressService addressService) {
        this.addressService=addressService;
        this.productRepository=productRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public Order save(ShoppingCart cart, long address_id, String paymentMethod, Double oldTotalPrice) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(cart.getCustomer());
        order.setTotalPrice(cart.getTotalPrice());
        order.setQuantity(cart.getTotalItems());
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(addressService.findByIdOrder(address_id));
        order.setAccept(false);
        order.setOrderStatus("Pending");
        if(oldTotalPrice != null){
            Double discount= oldTotalPrice - cart.getTotalPrice();
            String formattedDiscount = String.format("%.2f", discount);
            order.setDiscountPrice(Double.parseDouble(formattedDiscount));
        }
        List<OrderDetail> orderDetailList=new ArrayList<>();
        for(CartItem item : cart.getCartItems()){
            Product product=item.getProduct();
            int requestedQuantity = item.getQuantity();

            // Validate if requested quantity exceeds available quantity
            if (requestedQuantity > product.getCurrentQuantity()) {
                throw new IllegalArgumentException("Requested quantity exceeds available quantity for product: " + product.getName());
            }

            // Update the available quantity
            int newQuantity = product.getCurrentQuantity() - requestedQuantity;
            product.setCurrentQuantity(newQuantity);
            productRepository.save(product);

            // Create and save OrderDetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetail.setSize(item.getSize());
            orderDetail.setQuantity(requestedQuantity);
            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetails(orderDetailList);
        if(paymentMethod.equals("COD")) {
            order.setPaymentStatus("Pending");
            shoppingCartService.deleteCartById(cart.getId());
        }


        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(long order_id) {
        Order order=orderRepository.getById(order_id);
        Customer customer=order.getCustomer();

        List<OrderDetail>orderDetailList=order.getOrderDetails();
        for(OrderDetail orderDetail : orderDetailList){
            Product product = orderDetail.getProduct();
            if(product != null) {
                int currentQuantity = product.getCurrentQuantity();
                product.setCurrentQuantity(currentQuantity + orderDetail.getQuantity());
                productRepository.save(product);
            }
        }
        order.setOrderStatus("Cancelled");
        orderRepository.save(order);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void acceptOrder(long id) {
        Order order=orderRepository.getById(id);
        order.setAccept(true);
        Date oldDate = new Date();
        LocalDate localDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate newLocalDate = localDate.plusDays(5);
        Date newDate = Date.from(newLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        order.setDeliveryDate(newDate);
        order.setOrderStatus("Confirmed");
        orderRepository.save(order);
    }

    @Override
    public Order findOrderById(long id) {
        return orderRepository.getById(id);
    }

    @Override
    public List<Order> findAllOrdersByCustomer(long id) {
        return orderRepository.findAllBy(id);
    }



    @Override
    public void updatePayment(Order order, boolean status) {
        if(status){
            order.setPaymentStatus("Paid");
            orderRepository.save(order);
        }else {
            order.setPaymentStatus("Failed");
            orderRepository.save(order);
        }
    }

    @Override
    public void updateOrderStatus(String status, long order_id) {
        if(order_id != 0) {
            Order order = orderRepository.getReferenceById(order_id);
            if(status.equals("Shipped")){
                order.setOrderStatus(status);
                orderRepository.save(order);
            }else if(status.equals("Delivered")){
                order.setOrderStatus(status);
                if(order.getPaymentMethod().equals("COD")){
                    order.setPaymentStatus("Paid");
                }
                orderRepository.save(order);
            }
        }
    }

    @Override
    public void returnOrder(long id, Customer customer) {
        Order order=orderRepository.findById(id);
        order.setOrderStatus("Returned");
        orderRepository.save(order);

    }
}
