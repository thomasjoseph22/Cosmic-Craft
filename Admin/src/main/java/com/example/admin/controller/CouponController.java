package com.example.admin.controller;

import com.example.library.dto.CouponDto;
import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.service.CategoryService;
import com.example.library.service.CouponService;
import com.example.library.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CouponController {
    private ProductService productService;

    private CategoryService categoryService;

    private CouponService couponService;

    public CouponController(ProductService productService, CategoryService categoryService,
                            CouponService couponService) {
        this.couponService=couponService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/coupons")
    public String getCoupon(Principal principal, Model model){
        if(principal==null){
            return "redirect:/login";
        }
        List<CouponDto> couponDtoList=couponService.getAllCoupons();
        model.addAttribute("coupons",couponDtoList);
        model.addAttribute("size",couponDtoList.size());

        return "coupons";
    }


    @GetMapping("/coupons/add-coupon")
    public String getAddCoupon(Principal principal, Model model){
        if(principal==null){
            return "redirect:/login";
        }

        List<ProductDto> productList = productService.findAllProducts();
        List<Category> categoryList = categoryService.findAllByActivatedTrue();

        model.addAttribute("products",productList);
        model.addAttribute("categories",categoryList);
        model.addAttribute("coupon",new CouponDto());

        return "add-coupon";
    }

    @PostMapping("/coupons/save")
    public String addCoupon(@ModelAttribute("coupon")CouponDto coupon, RedirectAttributes redirectAttributes){

        try {
            // Check if a coupon with the same name already exists
            if (couponService.existsCouponByName(coupon.getCode())) {
                redirectAttributes.addFlashAttribute("error", "Coupon with the same code already exists!");
                return "redirect:/coupons/add-coupon";
            }

            // Save the new coupon if validation passes
            couponService.save(coupon);
            redirectAttributes.addFlashAttribute("success", "Added new Coupon successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new Coupon!");
        }

        return "redirect:/coupons";
    }


    @GetMapping("/coupons/update-coupon/{id}")
    public String updateCouponForm(@PathVariable("id") long id, Model model, Principal principal){

        if(principal==null){
            return "redirect:/login";
        }

        CouponDto couponDto=couponService.findById(id);
        model.addAttribute("coupon",couponDto);



        return "update-coupon";
    }


    @PostMapping("/coupons/update-coupon/{id}")
    public String updateCoupon(@ModelAttribute("coupon") CouponDto couponDto,
                               RedirectAttributes redirectAttributes){

        try {
            couponService.update(couponDto);
            redirectAttributes.addFlashAttribute("success", "Updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/coupons";
    }

    @GetMapping("/disable-coupon/{id}")
    public String disable(@PathVariable("id")long id,RedirectAttributes redirectAttributes){

        couponService.disable(id);
        redirectAttributes.addFlashAttribute("success","Coupon Disabled");
        return "redirect:/coupons";
    }

    @GetMapping("/enable-coupon/{id}")
    public String enable(@PathVariable("id")long id, RedirectAttributes redirectAttributes){


        couponService.enable(id);

        redirectAttributes.addFlashAttribute("success","Coupon Enabled");
        return "redirect:/coupons";
    }

    @GetMapping("/delete-coupon/{id}")
    public String delete(@PathVariable("id")long id,RedirectAttributes redirectAttributes){

        couponService.deleteCoupon(id);

        redirectAttributes.addFlashAttribute("success","Coupon deleted");

        return "redirect:/coupons";
    }
}
