package com.example.service;

import com.example.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryService {
    private final List<Category> categories = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    public CategoryService() {
        // Khởi tạo dữ liệu mẫu - chỉ 2 danh mục: điện thoại và laptop
        categories.add(new Category(idCounter.incrementAndGet(), "Điện thoại", "Điện thoại di động"));
        categories.add(new Category(idCounter.incrementAndGet(), "Laptop", "Máy tính xách tay"));
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public Category createCategory(Category category) {
        category.setId(idCounter.incrementAndGet());
        categories.add(category);
        return category;
    }

    public Optional<Category> updateCategory(Long id, Category categoryDetails) {
        Optional<Category> categoryOpt = getCategoryById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setName(categoryDetails.getName());
            category.setDescription(categoryDetails.getDescription());
            return Optional.of(category);
        }
        return Optional.empty();
    }

    public boolean deleteCategory(Long id) {
        return categories.removeIf(c -> c.getId().equals(id));
    }
}
