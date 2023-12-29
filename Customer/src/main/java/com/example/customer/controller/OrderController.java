package com.example.customer.controller;

import com.example.library.dto.AddressDto;
import com.example.library.model.*;
import com.example.library.service.AddressService;
import com.example.library.service.CustomerService;
import com.example.library.service.OrderService;
import com.example.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class OrderController {

    private CustomerService customerService;
    private OrderService orderService;
    private ShoppingCartService shoppingCartService;
    private AddressService addressService;



    public OrderController(CustomerService customerService, OrderService orderService, ShoppingCartService shoppingCartService,
                           AddressService addressService) {
        this.addressService=addressService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/check-out")
    public String checkOut(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByEmail(principal.getName());

            ShoppingCart cart = customerService.findByEmail(principal.getName()).getCart();
            Set<CartItem> cartItems=cart.getCartItems();
            List<Address> addressList = customer.getAddress();

            model.addAttribute("addressDto",new AddressDto());
            model.addAttribute("customer", customer);
            model.addAttribute("addressList", addressList);
            model.addAttribute("size",addressList.size());
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("page", "Check-Out");
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("grandTotal", cart.getTotalItems());
            return "checkout";
        }
    }




    @RequestMapping(value = "/add-order",method = RequestMethod.POST)
    @ResponseBody
    public String createOrder(@RequestBody Map<String,Object> data, Principal principal, HttpSession session, Model model)  {
        String paymentMethod = data.get("payment_Method").toString();
        Long address_id=Long.parseLong(data.get("addressId").toString());
        Double oldTotalPrice= (Double)session.getAttribute("totalPrice");
        Customer customer = customerService.findByEmail(principal.getName());
        ShoppingCart cart = customer.getCart();


            Order order = orderService.save(cart,address_id,paymentMethod,oldTotalPrice);
            session.removeAttribute("totalItems");
            session.removeAttribute("totalPrice");
            session.setAttribute("orderId",order.getId());
            model.addAttribute("order", order);
            model.addAttribute("page", "Order Detail");
            model.addAttribute("success", "Order Added Successfully");
            JSONObject options = new JSONObject();
            options.put("status","Cash");

            return options.toString();



    }




    @GetMapping("/order-confirmation")
    public String getOrderConfirmation(Model model,HttpSession session){

        Long order_id=(Long)session.getAttribute("orderId");
        Order order=orderService.findOrderById(order_id);
        String paymentMethod = order.getPaymentMethod();
        if (paymentMethod.equals("COD")){
            model.addAttribute("payment","Pending");
        }

        model.addAttribute("order", order);
        model.addAttribute("success", "Order Added Successfully");
        session.removeAttribute("orderId");

        return "order-detail";
    }


    @GetMapping("/orders")
    public String getOrder(Principal principal,Model model){
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByEmail(principal.getName());
            List<Order> orderList = orderService.findAllOrdersByCustomer(customer.getId());
            model.addAttribute("orders", orderList);
            model.addAttribute("title", "Order");
            model.addAttribute("page", "Order");
            return "orders";
        }
    }

    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id")long order_id, RedirectAttributes attributes){
        orderService.cancelOrder(order_id);
        attributes.addFlashAttribute("success", "Cancel order successfully!");
        return "redirect:/dashboard?tab=orders";
    }

    @GetMapping("/return-order/{id}")
    public String returnOrder(@PathVariable("id")long order_id, RedirectAttributes attributes,
                              Principal principal){
        Customer customer=customerService.findByEmail(principal.getName());
        orderService.returnOrder(order_id,customer);
        attributes.addFlashAttribute("success", "Order Returned successfully!");
        return "redirect:/dashboard?tab=orders";
    }


    @GetMapping("/order-view/{id}")
    public String orderView(@PathVariable("id")long order_id,Model model,HttpSession session){
        Order order=orderService.findOrderById(order_id);
        String paymentMethod = order.getPaymentMethod();
        if (paymentMethod.equals("COD")){
            model.addAttribute("payment","Pending");
        }

        Customer customer=customerService.findById(order.getCustomer().getId());
        Address address = addressService.findDefaultAddress(customer.getId());
        model.addAttribute("order",order);
        model.addAttribute("address",address);

        return "order-view";
    }

    @GetMapping("/order-tracking/{id}")
    public String orderTrack(@PathVariable("id")long order_id,Model model,HttpSession session){

        Order order=orderService.findOrderById(order_id);
        String paymentMethod = order.getPaymentMethod();
        if (paymentMethod.equals("COD")){
            model.addAttribute("payment","Pending");
        }

        Date currentTime = new Date();
        Customer customer=customerService.findById(order.getCustomer().getId());
        Address address = addressService.findDefaultAddress(customer.getId());
        if(order.getOrderStatus().equals("Pending")){
            int pending=1;
            model.addAttribute("pending",pending);
        }else if(order.getOrderStatus().equals("Confirmed")){
            int pending=1;
            int confirmed=2;
            model.addAttribute("pending",pending);
            model.addAttribute("confirmed",confirmed);
        }else if(order.getOrderStatus().equals("Shipped")){
            int pending=1;
            int confirmed=2;
            int shipped=3;
            model.addAttribute("pending",pending);
            model.addAttribute("confirmed",confirmed);
            model.addAttribute("shipped",shipped);
        }else if(order.getOrderStatus().equals("Delivered")){
            int pending=1;
            int confirmed=2;
            int shipped=3;
            int delivered=4;
            model.addAttribute("pending",pending);
            model.addAttribute("confirmed",confirmed);
            model.addAttribute("shipped",shipped);
            model.addAttribute("delivered",delivered);
        }
        model.addAttribute("order",order);
        model.addAttribute("time",currentTime);
        model.addAttribute("address",address);

        return "order-track";
    }
}
