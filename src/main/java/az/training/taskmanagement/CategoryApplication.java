package az.training.taskmanagement;

import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.inmemory.InMemoryCategoryRepository;
import az.training.taskmanagement.service.CategoryService;
import az.training.taskmanagement.service.impl.CategoryServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CategoryApplication {
    public static void main(String[] args) { SpringApplication.run(CategoryApplication.class, args); }
    @Bean
    CategoryRepository categoryRepository() { return new InMemoryCategoryRepository(); }
    @Bean
    CategoryService categoryService(CategoryRepository repository) { return new CategoryServiceImpl(repository); }
}
