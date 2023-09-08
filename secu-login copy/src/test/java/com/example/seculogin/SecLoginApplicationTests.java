package com.example.seculogin;

import com.example.seculogin.entity.Blog;
import com.example.seculogin.entity.Category;
import com.example.seculogin.entity.Role;
import com.example.seculogin.entity.User;
import com.example.seculogin.repository.BlogRepository;
import com.example.seculogin.repository.CategoryRepository;
import com.example.seculogin.repository.UserRepository;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
//import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootTest
class SecLoginApplicationTests {


    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private Faker faker;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Test
    void contextLoads() {
    }

    @Test
    void save_categories() {
        categoryRepository.save(new Category(null, "Backend"));
        categoryRepository.save(new Category(null, "Frontend"));
        categoryRepository.save(new Category(null, "Java"));
        categoryRepository.save(new Category(null, "C#"));
        categoryRepository.save(new Category(null, "DA"));
        categoryRepository.save(new Category(null, "BA"));
        categoryRepository.save(new Category(null, "Brse"));
        categoryRepository.save(new Category(null, "HR"));
    }

    @Test

    void save_users() {
        User user =  User.builder().name("Lai Duy Nghia").email("nghia@gmail.com").password(passwordEncoder.encode("111")).role(Role.ADMIN).build();
        User user1 =  User.builder().name("Nguyen Van An").email("an@gmail.com").password(passwordEncoder.encode("111")).role(Role.EDITOR).build();
        User user2 =  User.builder().name("Nguyen Van B").email("b@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user3 =  User.builder().name("Nguyen Van C").email("c@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user4 =  User.builder().name("Nguyen Van D").email("d@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user5 =  User.builder().name("Nguyen Van E").email("e@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user6 =  User.builder().name("Nguyen Van 6").email("6@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user7 =  User.builder().name("Nguyen Van 7").email("7@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user8 =  User.builder().name("Nguyen Van 8").email("8@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user9 =  User.builder().name("Nguyen Van 9").email("9@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user10 =  User.builder().name("Nguyen Van 10").email("10@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();
        User user11 =  User.builder().name("Nguyen Van 11").email("11@gmail.com").password(passwordEncoder.encode("111")).role(Role.USER).build();

        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        userRepository.save(user7);
        userRepository.save(user8);
        userRepository.save(user9);
        userRepository.save(user10);
        userRepository.save(user11);
    }

    @Test
    @Rollback(value = false)
    void save_blog() {
        Random rd = new Random();

        List<User> users = userRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        for (int i = 0; i < 12; i++) {
            // Random 1 user
            User rdUser = users.get(rd.nextInt(users.size()));

            // Random 1 ds category
            List<Category> rdCategories = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Category rdCategory = categories.get(rd.nextInt(categories.size()));
                if (!rdCategories.contains(rdCategory)) {
                    rdCategories.add(rdCategory);
                }
            }

            String title = faker.lorem().sentence(10);
            Blog blog = Blog.builder()
                    .title(title)
                    .description(faker.lorem().sentence(20))
                    .content(faker.lorem().sentence(100))
//                    .status(rd.nextInt(2) == 1)
                    .status(rd.nextInt(2))
                    .user(rdUser)
                    .categories(rdCategories)
                    .build();

            blogRepository.save(blog);
        }

        List<Category> categories1 = new ArrayList<>();
        categories1.add(categoryRepository.findById(1L).get());
        blogRepository.save(Blog.builder().title("Giới thiệu về Tôm").description("Đôi nét về Tôm - Nguyễn Gia Bảo").content(faker.lorem().sentence(100))
                .status(1).user(userRepository.findById(1L).get())
                .categories(categories1)
                .build());
    }

}
