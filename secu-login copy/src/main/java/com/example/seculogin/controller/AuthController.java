package com.example.seculogin.controller;

import com.example.seculogin.entity.User;
import com.example.seculogin.request.ChangePasswordRequest;
import com.example.seculogin.request.ForgotPasswordRequest;
import com.example.seculogin.request.LoginRequest;
import com.example.seculogin.security.ICurrentUser;
import com.example.seculogin.security.ICurrentUserImpl;
import com.example.seculogin.service.AuthService;
import com.example.seculogin.service.TokenConfirmService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    private final ICurrentUser iCurrentUser;
    @Autowired
    private TokenConfirmService tokenConfirmService;

    // Login page
    @GetMapping("/admin/login")
    public String getLoginPage() {
        User user = iCurrentUser.getUser();
        if(user != null) {
            return "redirect:/admin/blogs/own-blogs";
        }
        return "admin/util/login";
    }

    // Forgot password page
    @GetMapping("/admin/forgot-password")
    public String getForgotPasswordPage() {
        User user = iCurrentUser.getUser();
        if(user != null) {
            return "redirect:/admin/blogs/own-blogs";
        }
        return "admin/util/forgot-password";
    }

    // Change password page
    @GetMapping("/admin/change-password/{token}")
    public String getChangePasswordPage(@PathVariable String token, Model model) {
        model.addAttribute("info", tokenConfirmService.checkConfirmToken(token));
        model.addAttribute("token", token);
        return "admin/util/change-password";
    }


    // ========== API ==========
    // Login
    @PostMapping("/api/v1/admin/auth/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        return ResponseEntity.ok().body( authService.login(request, session));
    }

    // Forgot password
    @PostMapping("/api/v1/admin/auth/forgot-password")
    @ResponseBody
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        log.info("ForgotPasswordRequest : {}", request);
        authService.forgotPassword(request);
        return ResponseEntity.ok("Send mail success");
    }

    @PostMapping("/api/v1/admin/auth/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok("Change pass word success");
    }
}
