package com.example.seculogin.controller;


import com.example.seculogin.entity.Blog;
import com.example.seculogin.entity.Category;
import com.example.seculogin.entity.User;
import com.example.seculogin.repository.UserRepository;
import com.example.seculogin.request.BlogRequest;
import com.example.seculogin.security.ICurrentUser;
import com.example.seculogin.service.BlogService;
import com.example.seculogin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;
    private final ICurrentUser iCurrentUser;


    // Chi tiết bài viết
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    @GetMapping("api/v1/admin/blogs/{id}")
    @ResponseBody
    public ResponseEntity<?> getBlogDetailAdmin(@PathVariable Long id) {
        return ResponseEntity.ok().body(blogService.getBlogDetail(id));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    @GetMapping("admin/blogs/{id}/detail")
    public String getBlogDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getBlogDetail(id));
        model.addAttribute("categoryList", categoryService.getAll());
        return "admin/blog/blog-detail";
    }



    // Danh sách tất cả bài viết
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("api/v1/admin/blogs")
    @ResponseBody
    public ResponseEntity<?> getAllBlogApi(@RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false, defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(blogService.getAllBlog(page, pageSize));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("admin/blogs")
    public String getAllBlogPage(@RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "5") int pageSize,
                                 Model model) {
        User currentUser = iCurrentUser.getUser();
        model.addAttribute("user", currentUser);

        Page<Blog> pageInfo = blogService.getAllBlog(page, pageSize);
        model.addAttribute("page", pageInfo);

        model.addAttribute("blogs", pageInfo.getContent());

        int totalPages = pageInfo.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(1, pageInfo.getTotalPages() + 1).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
            System.out.println(pageNumbers);
        }
        model.addAttribute("currentPage", page);
        return "admin/blog/blog-index";
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    @GetMapping("/admin/blogs/own-blogs")
    public String getOwnBlogPage(Model model) {
        model.addAttribute("blogs", blogService.getAllOwnBlog());
        return "admin/blog/blog-yourself1";
    }


    // Thêm mới bài viết

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    @GetMapping("/admin/blogs/create")
    public String getBlogCreatePage(Model model) {

        List<Category> categoryList = categoryService.getAll();
        model.addAttribute("categoryList", categoryList);
        return "admin/blog/blog-create";
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    @PostMapping("/api/v1/admin/blogs")
    @ResponseBody
    public ResponseEntity<?> createBlog(@RequestBody BlogRequest request) {
        return new ResponseEntity<>(blogService.createBlog(request), HttpStatus.CREATED); // 201
    }


    // Cập nhật bài viết
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    @PutMapping("api/v1/admin/blogs/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody BlogRequest request) {
        return ResponseEntity.ok(blogService.updateBlog(id, request)); // 200
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    @GetMapping("api/v1/admin/blogs/blogs-title")
    @ResponseBody
    public ResponseEntity<?> getBlogByTitleApi(@RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = "5") int pageSize,
                                               @RequestParam String keyword) {
        return ResponseEntity.ok().body(blogService.getBlogByTitle(page, pageSize, keyword));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    @GetMapping("api/v1/admin/blogs/blogs-status")
    @ResponseBody
    public ResponseEntity<?> getBlogByStatusApi(@RequestParam(required = false, defaultValue = "1") int page,
                                                @RequestParam(required = false, defaultValue = "5") int pageSize,
                                                @RequestParam int status) {
        return ResponseEntity.ok().body(blogService.getBlogByStatus( page, pageSize, status));
    }


// ================== Annonymous ==================

    //@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR') or hasRole('ROLE_USER')")
    @GetMapping("api/v1/blogs/blogs-category")
    @ResponseBody
    public ResponseEntity<?> getBlogByCategoryNameApi(@RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = "5") int pageSize,
                                               @RequestParam String categoryName) {
        return ResponseEntity.ok().body(blogService.getBlogByCategory(page, pageSize, categoryName));
    }

    @GetMapping("api/v1/blogs")
    @ResponseBody
    public ResponseEntity<?> getBlogsActive(@RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(blogService.getAllBlogActive(page, pageSize));
    }

}
