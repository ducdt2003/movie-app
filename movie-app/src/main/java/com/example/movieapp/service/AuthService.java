package com.example.movieapp.service;

import com.example.movieapp.entity.User;
import com.example.movieapp.exception.BadRequestException;
import com.example.movieapp.model.request.LoginRequest;
import com.example.movieapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HttpSession session;

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Tài khoản hoặc mật khẩu không chính xác"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Tài khoản hoặc mật khẩu không chính xác");
        }

        // Lưu thông tin user vào session
        session.setAttribute("currentUser", user);
        return user;
    }

    public void logout() {
        session.removeAttribute("currentUser");
    }

    public User getCurrentUser() {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new BadRequestException("User chưa đăng nhập");
        }
        return currentUser;
    }
}
