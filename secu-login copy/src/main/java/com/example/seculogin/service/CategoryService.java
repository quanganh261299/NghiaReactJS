package com.example.seculogin.service;

import com.example.seculogin.dto.UserDto;
import com.example.seculogin.entity.Category;
import com.example.seculogin.exception.BadRequestException;
import com.example.seculogin.exception.NotFoundException;
import com.example.seculogin.repository.CategoryRepository;
import com.example.seculogin.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Page<Category> getAllCategory(Integer page, Integer pageSize) {
        Page<Category> categoryPage = categoryRepository.findAll(PageRequest.of(page - 1, pageSize));
        return categoryPage;
    }

    public void updateCategory(long id, CategoryRequest request) {
        Optional<Category> opCategory = categoryRepository.findById(id);
        if (opCategory.isPresent()) {
            Category curCategory = opCategory.get();
            curCategory.setName(request.getName());
            categoryRepository.save(curCategory);
        }
    }

    public Category createCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Category is exist");
        }
        Category category = new Category();
        category.setName(request.getName());

        categoryRepository.save(category);
        return category;
    }


    public void deleteCategory(Long id) {
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Danh mục không tồn tại!");
        }

        //Check product in category
        long count = categoryRepository.checkBlogInCategory(id);
        if (count > 0) {
            throw new BadRequestException("Có sản phẩm thuộc danh mục không thể xóa!");
        }

        try {
            categoryRepository.deleteById(id);
        } catch (Exception ex) {
            throw new BadRequestException("Lỗi khi xóa danh mục!");
        }
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }
}
