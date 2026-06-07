package com.example.omnichannel_orders_api.application.service;

import com.example.omnichannel_orders_api.application.dto.CategoryRequest;
import com.example.omnichannel_orders_api.application.dto.CategoryResponse;
import com.example.omnichannel_orders_api.domain.model.Category;
import com.example.omnichannel_orders_api.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> listActive() {
        return categoryRepository.findAllByActiveTrue()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public CategoryResponse getById(UUID id) {
        return categoryRepository.findByIdAndActiveTrue(id)
                .map(CategoryResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + id));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Category already exists: " + request.name());
        }

        Category category = Category.builder()
                .name(request.name())
                .description(request.description())
                .build();

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + id));

        if (categoryRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new IllegalArgumentException("Category already exists: " + request.name());
        }

        category.setName(request.name());
        category.setDescription(request.description());

        if (request.active() != null) {
            category.setActive(request.active());
        }

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public void deactivate(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + id));

        category.setActive(false);
        categoryRepository.save(category);
    }
}
