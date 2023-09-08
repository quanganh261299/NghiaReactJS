package com.example.seculogin.repository;

import com.example.seculogin.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query(value = "SELECT count(category_id) FROM blog_categories WHERE category_id = ?1", nativeQuery = true)
    int checkBlogInCategory(Long id);


    List<Category> findByIdIn(List<Long> categoryIds);
}
