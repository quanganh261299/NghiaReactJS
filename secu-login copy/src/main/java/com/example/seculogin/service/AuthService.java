package com.example.seculogin.service;

import com.example.seculogin.entity.TokenConfirm;
import com.example.seculogin.entity.User;
import com.example.seculogin.exception.BadRequestException;
import com.example.seculogin.exception.NotFoundException;
import com.example.seculogin.repository.TokenConfirmRepository;
import com.example.seculogin.repository.UserRepository;
import com.example.seculogin.request.ChangePasswordRequest;
import com.example.seculogin.request.ForgotPasswordRequest;
import com.example.seculogin.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenConfirmRepository tokenConfirmRepository;


    public ResponseEntity<?> login(LoginRequest request, HttpSession session) {
        // Tạo đối tượng xác thực
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            // Tiến hành xác thực
            Authentication authentication = authenticationManager.authenticate(token);

            // Lưu dữ liệu vào context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Tạo ra session
            session.setAttribute("MY_SESSION", authentication.getName());

            return ResponseEntity.ok(authentication.getPrincipal());
//            return "Đăng nhập thành công";
        } catch (Exception e) {
            throw new BadRequestException("Login fail! Please check your email and password again ");
        }
    }

    // Forgot password
    public void forgotPassword(ForgotPasswordRequest request) {
        // Kiểm tra email gửi lên có tồn tại trong db hay không
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found user");
                });

        // Tạo ra token -> lưu vào cơ sở dữ liệu
        // Token là chuỗi UUID
        TokenConfirm tokenConfirm = TokenConfirm.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .user(user)
                .build();
        tokenConfirmRepository.save(tokenConfirm);
        // Send email chứa token
        // Link : http://localhost:8080/doi-mat-khau/hakdiwowjkdkdkdjfffki
        mailService.sendMail(
                user.getEmail(),
                "Forgot password",
                "Access this link to change your password:\n" + "http://localhost:8089/admin/change-password/" + tokenConfirm.getToken()
        );
    }

    //Change password
    public void changePassword(ChangePasswordRequest request) {
        // Kiểm tra token
        TokenConfirm tokenConfirm = tokenConfirmRepository.findByToken(request.getToken())
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found token");
                });

        // Kiểm tra password và confirm password
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("The new password and the confirmation password are not same, please check again! ");
        }

        // Lấy thông tin của user
        User user = tokenConfirm.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
