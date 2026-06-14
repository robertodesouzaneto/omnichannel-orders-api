package com.example.omnichannel_orders_api.api.controller;

import com.example.omnichannel_orders_api.application.dto.PageResponse;
import com.example.omnichannel_orders_api.application.dto.ProductRequest;
import com.example.omnichannel_orders_api.application.dto.ProductResponse;
import com.example.omnichannel_orders_api.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Products", description = "Menu product management")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "List all active products (paginated)")
    public PageResponse<ProductResponse> listActive(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return productService.listActive(pageable);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "List active products by category (paginated)")
    public PageResponse<ProductResponse> listByCategory(
            @PathVariable UUID categoryId,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return productService.listByCategory(categoryId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ProductResponse getById(@PathVariable UUID id) {
        return productService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new product (MANAGER or ADMIN)")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product (MANAGER or ADMIN)")
    public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate a product (MANAGER or ADMIN)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable UUID id) {
        productService.deactivate(id);
    }
}
