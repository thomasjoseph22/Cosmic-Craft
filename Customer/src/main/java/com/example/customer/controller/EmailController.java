package com.example.customer.controller;

import com.example.library.model.UserOtp;
import com.example.library.service.CustomerService;
import com.example.library.service.EmailService;
import com.example.library.service.OtpService;
import com.example.library.service.UserOtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class EmailController {

    private OtpService otpService;
    private EmailService emailService;
    private UserOtpService userOTPService;
    private CustomerService usersSevice;
    private PasswordEncoder passwordEncoder;

    public EmailController(OtpService otpService,
                               EmailService emailService,
                               UserOtpService userOTPService,
                               CustomerService usersSevice,
                               PasswordEncoder passwordEncoder) {
        this.otpService = otpService;
        this.emailService = emailService;
        this.userOTPService = userOTPService;
        this.usersSevice = usersSevice;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/verifyEmail")
    public String showVerifyEmail(){
        return "verifyEmail";
    }
    @GetMapping("/otpvalidation")
    public String showotpvalidationPage(Model model, HttpSession session){
        String email = (String) model.asMap().get("email");
        UserOtp userOTP = new UserOtp();
        userOTP.setEmail(email);
        session.setAttribute("email",email);
        model.addAttribute("userOTP",userOTP);
        return "otpvalidation";
    }

    @PostMapping("/sendVerificationEmailOtp")
    public String sendVerificationEmailOtp(
            @RequestParam("email")String email
            , HttpSession session,
            RedirectAttributes redirectAttributes) throws Exception {

        if(usersSevice.findByEmail(email)==null){
            String otp = otpService.generateOTP();
            if(!userOTPService.existsByEmail(email)){
                // new email verification
                UserOtp userOTP =new UserOtp();
                userOTP.setEmail(email);
                userOTP.setOneTimePassword(passwordEncoder.encode(otp));
                userOTP.setCreatedAt(new Date());
                userOTP.setOtpRequestedTime(new Date());
                userOTP.setUpdateOn(new Date());
                try{
                    userOTPService.saveOrUpdate(userOTP);
                }catch(Exception e){
                    e.printStackTrace();
                    throw new Exception("Couldn't finish OTP verification process"+ HttpStatus.BAD_REQUEST);
                }

            }else{
                //code to delete all data related to this email id
                UserOtp userOTP=userOTPService.findByEmail(email);
                userOTP.setOneTimePassword(passwordEncoder.encode(otp));
                userOTP.setOtpRequestedTime(new Date());
                userOTP.setUpdateOn(new Date());
                try{
                    userOTPService.saveOrUpdate(userOTP);
                }catch(Exception e){
                    e.printStackTrace();
                    throw new Exception("Couldn't finish OTP verification process");
                }
            }
            String status = emailService.sendSimpleMail(email,otp);
            if(status.equals("success")){
                session.setAttribute("message","otpsent");
                redirectAttributes.addFlashAttribute("email",email);
                return "redirect:/otpvalidation";

            }else{
                return "redirect:/verifyEmail?error";
            }
        }else{
            return "redirect:/verifyEmail?existUser";
        }

    }
    @PostMapping("/sendEmailOTPLogin")
    public String sendEmailOTPLogin(
            @RequestParam("email")String email
            , HttpSession session,
            RedirectAttributes redirectAttributes) throws Exception {
        if(usersSevice.findByEmail(email)!=null){
            String otp = otpService.generateOTP();
            UserOtp userOTP = userOTPService.findByEmail(email);
            if(userOTP!=null){
                userOTP.setOneTimePassword(passwordEncoder.encode(otp));
                userOTP.setOtpRequestedTime(new Date());
                userOTP.setUpdateOn(new Date());
            }else{
                userOTP = new UserOtp();
                userOTP.setEmail(email);
                userOTP.setOneTimePassword(passwordEncoder.encode(otp));
                userOTP.setCreatedAt(new Date());
                userOTP.setOtpRequestedTime(new Date());
                userOTP.setUpdateOn(new Date());
            }
            try{
                userOTPService.saveOrUpdate(userOTP);
            }catch(Exception e){
                e.printStackTrace();
                throw new Exception("Send OTP.Please try after some time...");
            }
            String status = emailService.sendSimpleMail(email,otp);
            if(status.equals("success")){
                session.setAttribute("message","otpsent");
                redirectAttributes.addFlashAttribute("email",email);
                return "redirect:/forgotPasswordOTPLogin";

            }else{
                return "redirect:/forgotpassword?error";
            }
        }else{
            return "redirect:/forgotpassword?error";
        }
    }
    @PostMapping("/validateOTP")
    public String validateOTP(@ModelAttribute("userOTP") UserOtp userOTPRequest, HttpSession session,
                              RedirectAttributes redirectAttributes){
        String email=session.getAttribute("email").toString();
        UserOtp userOTP = userOTPService.findByEmail(userOTPRequest.getEmail());

        if (userOTP != null && passwordEncoder.matches(userOTPRequest.getOneTimePassword(), userOTP.getOneTimePassword())) {
            //navigate to signup page
            redirectAttributes.addFlashAttribute("email", userOTP.getEmail());
            return "redirect:/register";
        } else {
            return "redirect:/otpvalidation?error";
        }
    }
}
