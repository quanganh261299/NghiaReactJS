package com.example.seculogin.security;

import com.example.seculogin.entity.User;
import com.example.seculogin.exception.NotFoundException;
import com.example.seculogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ICurrentUserImpl implements ICurrentUser{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> {
                throw new NotFoundException("Not found user");
            });
        }
        return null;
    }
}
