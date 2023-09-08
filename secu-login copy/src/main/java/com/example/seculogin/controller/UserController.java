package com.example.seculogin.controller;

import com.example.seculogin.dto.UserDto;
import com.example.seculogin.entity.User;
import com.example.seculogin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;

//    @PreAuthorize(has)
    @GetMapping("admin/list-user")
    public String getListUserPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            Model model) {

        Page<UserDto> pageInfo = userService.getAllUser(page, pageSize);
        model.addAttribute("page", pageInfo);

        model.addAttribute("users", pageInfo.getContent());

        int totalPages = pageInfo.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(1, pageInfo.getTotalPages() + 1).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
            System.out.println(pageNumbers);
        }
        model.addAttribute("currentPage", page);
        return "users";
    }

//    @GetMapping("admin/users")
//    public String getListUserPage1(
//            @RequestParam(required = false, defaultValue = "1") int page,
//            @RequestParam(required = false, defaultValue = "10") int pageSize,
//            Model model) {
//
//        Page<UserDto> pageInfo = userService.getAllUser(page, pageSize);
//        model.addAttribute("page", pageInfo);
//
//        model.addAttribute("users", pageInfo.getContent());
//
//        int totalPages = pageInfo.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.range(1, pageInfo.getTotalPages() + 1).boxed().toList();
//            model.addAttribute("pageNumbers", pageNumbers);
//            System.out.println(pageNumbers);
//        }
//        model.addAttribute("currentPage", page);
//        return "admin/blog/blog-index";
//    }

    @GetMapping("api/v1/users")
    @ResponseBody
    public ResponseEntity<?> getListUserPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize
            ) {


        return ResponseEntity.ok().body(userService.getAllUser(page, pageSize));
    }

    @GetMapping("/admin/user/{id}")
    public String getDetailUserPage(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "admin/user";
    }

    @GetMapping("api/v1/user/{id}")
    @ResponseBody
    public ResponseEntity<?> getDetailUser(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("admin/create-user")
    public String getCreateUserPage() {
        return "admin/user/user-create";
    }
}
