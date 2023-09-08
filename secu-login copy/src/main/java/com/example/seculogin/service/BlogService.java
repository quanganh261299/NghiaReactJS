package com.example.seculogin.service;

import com.example.seculogin.entity.Blog;
import com.example.seculogin.entity.Category;
import com.example.seculogin.entity.User;
import com.example.seculogin.exception.BadRequestException;
import com.example.seculogin.exception.NotFoundException;
import com.example.seculogin.repository.BlogRepository;
import com.example.seculogin.repository.CategoryRepository;
import com.example.seculogin.repository.UserRepository;
import com.example.seculogin.request.BlogRequest;
import com.example.seculogin.security.ICurrentUser;
import com.example.seculogin.security.ICurrentUserImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private final ICurrentUser iCurrentUser;

    public Page<Blog> getAllBlogActive(Integer page, Integer pageSize) {
        return blogRepository.findBlogsActive(PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending()));
    }

    public Page<Blog> getAllBlog(Integer page, Integer pageSize) {
        return blogRepository.findBlogs(PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending()));
    }

    // ADMIN-EDITOR
    public Blog getBlogDetail(Long id) {
         Optional<Blog> result = blogRepository.findById(id);
         if (result.isEmpty()) {
             throw new NotFoundException("Blog with id-" + id + "doesn't exist");
         }
         return result.get();
    }

    // Annonymous
    public Blog getBlogDetailById(Long id) {
        Optional<Blog> result = blogRepository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Blog with id-" + id + "doesn't exist");
        }

        Blog blog = result.get();
         if (blog.getStatus() == 0) {
             throw new NotFoundException("Blog with id-" + id + "doesn't exist");
         }
        return blog;
    }

    public Page<Blog> getBlogByTitle(Integer page, Integer pageSize, String title) {

        return blogRepository.searchBlogByTitle(PageRequest.of(page-1, pageSize), title);
    }

    public Page<Blog> getBlogByCategory(Integer page, Integer pageSize, String categoryName) {
        return blogRepository.findByCategories_NameLikeIgnoreCase(categoryName, PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending()));
    }

    public Page<Blog> getBlogByStatus(Integer page, Integer pageSize, int status) {
        return blogRepository.getBlogsByStatus(PageRequest.of(page-1, pageSize), status);
    }

    @Transactional
    public Blog createBlog(BlogRequest request) {

        User curUser = iCurrentUser.getUser();
        User user = userRepository.findByEmail(curUser.getEmail()).get();
        log.info("email cur account = {}", curUser.getRole());
//        User curUser1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Category> categories = categoryRepository.findByIdIn(request.getCategoryIds());

        Blog blog = Blog.builder()
                .title(request.getTitle()).description(request.getDescription())
                .content(request.getContent()).categories(categories)
                .status(request.getStatus()).user(user)
                .modifiedAt(LocalDateTime.now())
                .build();
        blogRepository.save(blog);
        return blog;
    }


    @Transactional
    public Blog updateBlog(Long id, BlogRequest request) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found blog with id = " + id);
        });

        List<Category> categories = categoryRepository.findByIdIn(request.getCategoryIds());
        blog.setTitle(request.getTitle());
        blog.setDescription(request.getDescription());
        blog.setContent(request.getContent());
        blog.setStatus(request.getStatus());
        blog.setCategories(categories);
        blog.setModifiedAt(LocalDateTime.now());
        blogRepository.save(blog);

        return blog;
    }

//    public Page<Blog> getAllOwnBlog(Integer page, Integer pageSize) {
//        User curUserLogin = iCurrentUser.getUser();
//        User user = userRepository.findByEmail(curUserLogin.getEmail()).get();
//        return blogRepository.findByUser_IdOrderByCreatedAtDesc(user.getId(), PageRequest.of(page, pageSize));
//    }

    public List<Blog> getAllOwnBlog() {
        User curUserLogin = iCurrentUser.getUser();
        User user = userRepository.findByEmail(curUserLogin.getEmail()).get();
        return user.getBlogs();
    }


}
