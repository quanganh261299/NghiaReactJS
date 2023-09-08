package com.example.seculogin.repository;

import com.example.seculogin.entity.Blog;
import com.example.seculogin.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query(value = "select b from Blog b where b.status = 1")
    Page<Blog> findBlogsActive(Pageable pageable);

    @Query(value = "select b from Blog b")
    Page<Blog> findBlogs(Pageable pageable);

    @Query(value = "SELECT * FROM blog b WHERE b.title LIKE CONCAT('%',:keyword,'%')", nativeQuery = true)
    Page<Blog> searchBlogByTitle(Pageable pageable, @Param("keyword") String keyword);

    @Query(value = "SELECT * FROM blog  INNER JOIN blog_categories  ON blog.id = blog_categories.blog_id INNER JOIN category  ON blog_categories.category_id = category.id WHERE category.name = :categoryName", nativeQuery = true)
    Page<Blog> searchBlogByCategory(Pageable pageable, @Param("categoryName") String categoryName);

//    @Query("")
//    Page<Blog> getBlogByCategory(Pageable pageable, @Param("categoryName") String categoryName);

    @Query("select b from Blog b inner join b.categories categories where upper(categories.name) like upper(?1)")
    Page<Blog> findByCategories_NameLikeIgnoreCase(String name, Pageable pageable);

    @Query("select b from Blog b where b.status = :status")
    Page<Blog> getBlogsByStatus(Pageable pageable, @Param("status") int status);


    //@Query("select b from Blog b where b.user.id = ?1 order by b.createdAt DESC")
//    @Query(value = "select * from blog b where b.user_id = ?1 order by b.created_at DESC", nativeQuery = true)
//    Page<Blog> findByUser_IdOrderByCreatedAtDesc(Long id, Pageable pageable);
//    List<Blog>
}
