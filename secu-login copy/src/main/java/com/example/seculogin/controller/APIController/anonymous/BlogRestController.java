package com.example.seculogin.controller.APIController.anonymous;

import com.example.seculogin.security.ICurrentUser;
import com.example.seculogin.service.BlogService;
import com.example.seculogin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/")
@RequiredArgsConstructor
public class BlogRestController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    private final ICurrentUser iCurrentUser;

    // Danh sách các blog có status = 1 (active)
    @GetMapping("blogs")
    public ResponseEntity<?> getAllBlogApi(@RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false, defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(blogService.getAllBlogActive(page, pageSize));
    }

    // Chi tiết blog
    @GetMapping("blogs/{id}/detail")
    public ResponseEntity<?> getBlogDetailApi(@PathVariable Long id) {
        return ResponseEntity.ok().body(blogService.getBlogDetail(id));
    }

    // Danh sách category
    @GetMapping("/categories")
    public ResponseEntity<?> getBlogDetailApi(@RequestParam(required = false, defaultValue = "1") int page,
                                              @RequestParam(required = false, defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(categoryService.getAllCategory(page, pageSize));
    }

    // Danh sách blog theo category
    @GetMapping("blogs/category")
    @ResponseBody
    public ResponseEntity<?> getBlogByCategoryApi(@RequestParam(required = false, defaultValue = "1") int page,
                                                  @RequestParam(required = false, defaultValue = "5") int pageSize,
                                                  @RequestParam String categoryName) {
        return ResponseEntity.ok().body(blogService.getBlogByCategory(page, pageSize, categoryName));
    }
}
