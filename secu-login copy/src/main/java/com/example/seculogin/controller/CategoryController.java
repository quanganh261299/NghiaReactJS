package com.example.seculogin.controller;

import com.example.seculogin.entity.Category;
import com.example.seculogin.request.CategoryRequest;
import com.example.seculogin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/v1/admin/categories")
    @ResponseBody
    public ResponseEntity<?> getAllCategory(@RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "5") int pageSize) {

        return ResponseEntity.ok().body(categoryService.getAllCategory(page, pageSize));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("api/v1/admin/categories")
//    @ResponseBody
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok().body(categoryService.createCategory(request));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("api/v1/admin/categories/{id}")
//    @ResponseBody
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok().body("Cập nhật thành công");
    }

    @DeleteMapping("api/v1/admin/categories/{id}")
//    @ResponseBody
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }



    // Danh sách tất cả category
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("admin/categories")
    public String getListCategoryPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                      Model model) {
        Page<Category> pageInfo = categoryService.getAllCategory(page, pageSize);
        model.addAttribute("page", pageInfo);

        model.addAttribute("categoryList", pageInfo.getContent());

        int totalPages = pageInfo.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(1, pageInfo.getTotalPages() + 1).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
            System.out.println(pageNumbers);
        }
        model.addAttribute("currentPage", page);
        return "admin/category/category-list";
    }

//    // Chi tiết category
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("admin/categories/{id}")
//    public String getCategoryDetailPage(@PathVariable Long id, Model model) {
////        Page<UserDto> pageInfo = userService.getAllUser(page, pageSize);
////        model.addAttribute("page", pageInfo);
//
//        model.addAttribute("category", categoryService.findById(id));
//
//        return "admin/blog/blog-detail";
//    }


}
