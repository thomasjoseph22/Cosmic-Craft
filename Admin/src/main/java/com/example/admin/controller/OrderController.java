package com.example.admin.controller;

import com.example.library.model.Address;
import com.example.library.model.Customer;
import com.example.library.model.Order;
import com.example.library.service.AddressService;
import com.example.library.service.CustomerService;
import com.example.library.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    private OrderService orderService;

    private CustomerService customerService;

    private AddressService addressService;

    public OrderController(OrderService orderService, CustomerService customerService, AddressService addressService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.addressService = addressService;
    }

    @GetMapping("/orders")
    public String getOrders(Principal principal, Model model,
                            @RequestParam(name = "status",required = false,defaultValue = "")String orderStatus,
                            @RequestParam(name = "orderId",required = false,defaultValue = "0")long order_id){

        if(principal==null){
            return "redirect:/login";
        }else{
            System.out.println(orderStatus + order_id);
            orderService.updateOrderStatus(orderStatus,order_id);
            List<Order> orders=orderService.findAllOrders();
            model.addAttribute("orders",orders);

            return "order";

        }

    }

    @GetMapping("/accept-order/{id}")
    public String acceptOrder(@PathVariable("id")long order_id, RedirectAttributes attributes){

        orderService.acceptOrder(order_id);
        attributes.addFlashAttribute("success","Order Accepted");
        return "redirect:/orders";
    }



    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id")long order_id, RedirectAttributes attributes){
        orderService.cancelOrder(order_id);
        attributes.addFlashAttribute("success", "Order Cancelled successfully!");
        return "redirect:/orders";
    }


    @GetMapping("/order-view/{id}")
    public String orderView(@PathVariable("id")long order_id,Model model){
        Order order=orderService.findOrderById(order_id);
        Customer customer=customerService.findById(order.getCustomer().getId());
        Address address = addressService.findDefaultAddress(customer.getId());
        model.addAttribute("order",order);
        model.addAttribute("address",address);

        return "order-view";
    }
}
