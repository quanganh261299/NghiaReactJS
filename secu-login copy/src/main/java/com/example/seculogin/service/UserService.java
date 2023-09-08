package com.example.seculogin.service;

import com.example.seculogin.dto.UserDto;
import com.example.seculogin.entity.User;
import com.example.seculogin.repository.UserRepository;
import com.example.seculogin.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<UserDto> getAllUser(Integer page, Integer pageSize) {
        Page<UserDto> pageListUser = userRepository.findUsers(PageRequest.of(page - 1, pageSize));
        return pageListUser;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(UserRequest request) {
        User newUser = User.builder().name(request.getName()).email(request.getEmail()).password(request.getPassword()).role(request.getRole()).build();
        return userRepository.save(newUser);
    }




}
