package com.example.seculogin.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogRequest {
    @NotEmpty(message = "Nội dung rỗng")
    @Size(min = 1, max = 300, message = "Độ dài tiêu đề từ 1 - 300 ký tự")
    private String title;
    @NotEmpty(message = "Nội dung rỗng")
    private String content;
    private String description;
    private List<Long> categoryIds; // Danh sách id của các category áp dụng
    private int status;
}
