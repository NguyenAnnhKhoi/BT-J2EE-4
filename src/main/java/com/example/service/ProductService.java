package com.example.service;

import com.example.model.Product;
import com.example.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Autowired
    private CategoryService categoryService;

    public ProductService() {
        // Khởi tạo dữ liệu mẫu - chỉ có điện thoại và laptop
        Product p1 = new Product(idCounter.incrementAndGet(), "iPhone 15 Pro",
                "Điện thoại iPhone 15 Pro 256GB", 27990000.0, 50, 1L);
        Product p2 = new Product(idCounter.incrementAndGet(), "Samsung Galaxy S24",
                "Điện thoại Samsung Galaxy S24 Ultra", 29990000.0, 30, 1L);
        Product p3 = new Product(idCounter.incrementAndGet(), "MacBook Pro M3",
                "Laptop MacBook Pro 14 inch M3", 45990000.0, 20, 2L);
        Product p4 = new Product(idCounter.incrementAndGet(), "Dell XPS 15",
                "Laptop Dell XPS 15 inch", 35990000.0, 15, 2L);

        products.add(p1);
        products.add(p2);
        products.add(p3);
        products.add(p4);
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>(products);
        // Gán tên danh mục cho mỗi sản phẩm
        for (Product product : allProducts) {
            categoryService.getCategoryById(product.getCategoryId())
                    .ifPresent(category -> product.setCategoryName(category.getName()));
        }
        return allProducts;
    }

    public Optional<Product> getProductById(Long id) {
        Optional<Product> productOpt = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            categoryService.getCategoryById(product.getCategoryId())
                    .ifPresent(category -> product.setCategoryName(category.getName()));
        }
        return productOpt;
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return products.stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public Product createProduct(Product product) {
        product.setId(idCounter.incrementAndGet());
        products.add(product);
        return product;
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        Optional<Product> productOpt = getProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            product.setCategoryId(productDetails.getCategoryId());
            product.setImageUrl(productDetails.getImageUrl());
            return Optional.of(product);
        }
        return Optional.empty();
    }

    public boolean deleteProduct(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    public List<Product> searchProducts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword) ||
                        (p.getDescription() != null && p.getDescription().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }
}
