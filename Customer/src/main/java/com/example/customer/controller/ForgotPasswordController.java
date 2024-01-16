package com.example.customer.controller;

import com.example.library.Exception.CustomerNotFoundException;
import com.example.library.model.Customer;
import com.example.library.service.CustomerService;
import com.example.library.utils.Utility;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
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
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);


    @Autowired
    private CustomerService customerService;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/forgot_password")
    public String showforgotpassword(Model model){
        model.addAttribute("pagetitle","Forgot password");
        return "forgot_password";
    }


    @PostMapping("/forgot_password")
    public String processForgotpasswordform(HttpServletRequest request,Model model)  {
        String email = request.getParameter("email");
          String token  = RandomString.make(45);

        try {
            customerService.updateResetPasswordToken(token,email);
            String resetPasswordLink= Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email,resetPasswordLink);
            model.addAttribute("message","We have send a reset password link to your email. Please check your email");

        } catch (CustomerNotFoundException ex) {
                model.addAttribute("error",ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error","error while sending the message");
        }
        model.addAttribute("pageTitle","Forgot password");
        return "forgot_password";
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

         helper.setFrom("tmj949474@gmail.com","Cosmic-Craft");
         helper.setTo(email);
         String subject="The Link to reset the password of your account";
         String content = "<p>Hello,</p>"+
                 "<p>You have requested to reset your password</p>"+
                 "<p>Click the link below to change your password</p>"
                 +"<p><b><a href=\""+ resetPasswordLink  + "\"> Change my password </a></b></p>"
                 + "<p>Ignore the email if you do remember your password</p>";

                helper.setSubject(subject);
                helper.setText(content,true);

                javaMailSender.send(message);

    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        logger.info("Token in showResetPasswordForm: " + token);
        Customer customer = customerService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        logger.info("Token in processResetPassword: " + token);
        String password = request.getParameter("password");

        Customer customer = customerService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            customerService.updatePassword(customer, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "message";
    }


}
