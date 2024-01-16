package com.example.customer.controller;

import com.example.library.model.Customer;
import com.example.library.model.Wishlist;
import com.example.library.service.CustomerService;
import com.example.library.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/wishlist")
    public String getWishList(Principal principal, Model model){
        if(principal==null){
            return "redirect:/login";
        }
        Customer customer=customerService.findByEmail(principal.getName());
        List<Wishlist> wishlists=wishlistService.findAllByCustomer(customer);

        if (wishlists.isEmpty()) {
            model.addAttribute("check","You don't have any items in your WishList");

        }

        model.addAttribute("wishlists",wishlists);


        return "wishlist";
    }


    @PostMapping("/add-wishlist")
    @ResponseBody
    public ResponseEntity<String> addToWishlist(Principal principal, @RequestParam("productId") long productId) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }



        Customer customer = customerService.findByEmail(principal.getName());

        boolean exists = wishlistService.findByProductId(productId, customer);


        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already exists in wishlist");
        }

        wishlistService.save(productId,customer);

        return ResponseEntity.status(HttpStatus.OK).body("Product added to wishlist successfully  ");
    }

    @GetMapping("/delete-wishlist/{id}")
    public String delete(@PathVariable("id")long wishlistId, RedirectAttributes redirectAttributes){

        wishlistService.deleteWishlist(wishlistId);

        redirectAttributes.addFlashAttribute("success","Removed Successfully");

        return "redirect:/wishlist";
    }
}
