package com.example.omnichannel_orders_api.application.service;

import com.example.omnichannel_orders_api.application.dto.PageResponse;
import com.example.omnichannel_orders_api.application.dto.ProductRequest;
import com.example.omnichannel_orders_api.application.dto.ProductResponse;
import com.example.omnichannel_orders_api.domain.model.Category;
import com.example.omnichannel_orders_api.domain.model.Product;
import com.example.omnichannel_orders_api.infrastructure.repository.CategoryRepository;
import com.example.omnichannel_orders_api.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public PageResponse<ProductResponse> listActive(Pageable pageable) {
        return PageResponse.from(
                productRepository.findAllByActiveTrue(pageable)
                        .map(ProductResponse::from)
        );
    }

    public PageResponse<ProductResponse> listByCategory(UUID categoryId, Pageable pageable) {
        return PageResponse.from(
                productRepository.findAllByCategoryIdAndActiveTrue(categoryId, pageable)
                        .map(ProductResponse::from)
        );
    }

    public ProductResponse getById(UUID id) {
        return productRepository.findByIdAndActiveTrue(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findByIdAndActiveTrue(request.categoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + request.categoryId()));

        if (productRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Product already exists: " + request.name());
        }

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .category(category)
                .imageUrl(request.imageUrl())
                .build();

        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));

        if (productRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new IllegalArgumentException("Product already exists: " + request.name());
        }

        Category category = categoryRepository.findByIdAndActiveTrue(request.categoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + request.categoryId()));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);
        product.setImageUrl(request.imageUrl());

        if (request.active() != null) {
            product.setActive(request.active());
        }

        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public void deactivate(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));

        product.setActive(false);
        productRepository.save(product);
    }
}
