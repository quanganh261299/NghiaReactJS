package com.example.seculogin.request;

import com.example.seculogin.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {
    @NotEmpty(message = "Nội dung rỗng")
    private String name;
    @NotEmpty(message = "Nội dung rỗng")
    private String email;
    @NotEmpty(message = "Nội dung rỗng")
    private String password;
    private Role role;
}
