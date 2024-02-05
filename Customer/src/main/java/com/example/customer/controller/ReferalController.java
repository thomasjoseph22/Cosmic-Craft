package com.example.customer.controller;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;
import com.example.library.service.CustomerService;
import com.example.library.service.WalletService;
import com.example.library.utils.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ReferalController {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    CustomerService customerService;
    @Autowired
    WalletService walletService;
    @PostMapping("/sendReferal")
    public String processReferalCode(HttpServletRequest request, Model model,Principal principal) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        String token = RandomString.make(5);
        String username=principal.getName();
        customerService.updateReferalCodeToken(token, username);
        String referalLink = Utility.getSiteURL(request) + "/referal_link?token=" + token;
        sendEmail(email, referalLink);
        model.addAttribute("message", "We have sent a referal link to  email. Please check.");
        return "redirect:/dashboard";
    }
    @GetMapping("/referal_link")
    public String showReferalLogin(@Param(value = "token") String token, Model model) {
        CustomerDto customerDto=new CustomerDto();
        model.addAttribute("token", token);
        model.addAttribute("users",customerDto);
        return "Referral-register";
    }
    @PostMapping("/registration1")
    public String showRegisterReferalUser(@ModelAttribute("users") CustomerDto customerDto,
                                          HttpServletRequest request) {
        String token = request.getParameter("token");
        Optional<List<Customer>> customer = customerService.getByReferalToken(token);
        if (customer.isPresent() && !customer.get().isEmpty()) {
            List<Customer> customerList = customer.get();
            Customer referrer = customerService.findByEmail(customerDto.getEmail());
            Customer customerByEmail = customerService.findByEmail(customerDto.getEmail());
            if (customerByEmail != null) {
                return "redirect:/referal_link?exist&token=" + token;
            }
            Customer referredCustomer = customerService.save(customerDto);
            boolean referralOfferSuccess = false;
            try {
                walletService.addWalletToReferalEarn(referredCustomer.getId());
                System.out.println(referredCustomer.getEmail());
                referralOfferSuccess = true;
            } catch (Exception e) {
                // Log or handle the exception as needed
                e.printStackTrace();
            }

            if (referralOfferSuccess) {
                return "redirect:/referal_link?success";
            } else {
                // Handle failure, perhaps redirect to an error page
                return "redirect:/error";
            }
        } else {
            return "redirect:/referal_link?customerNotFound";
        }
    }


    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("tmj949474@gmail.com", "Cosmic-Craft");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to login Cosmic-Craft";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to Login, Cosmic-Craft.</p>"
                + "<p>Click the link below to log-in and signup page:</p>"
                + "<p><a href=\"" + link + "\">Cosmic-Craft</a></p>"
                + "<br>"
                + "<p>Ignore this email if you already have account, "
                + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
