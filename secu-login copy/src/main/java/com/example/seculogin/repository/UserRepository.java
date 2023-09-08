package com.example.seculogin.repository;

import com.example.seculogin.dto.UserDto;
import com.example.seculogin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "select new com.example.seculogin.dto.UserDto(u.id, u.name, u.email, u.role) from User u")
    Page<UserDto> findUsers(Pageable pageable);

    User getUserById(Long id);

}
