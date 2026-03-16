package com.devcrew.moodcode.domain.product.controller;

import com.devcrew.moodcode.domain.product.dto.ProductDetailResponse;
import com.devcrew.moodcode.domain.product.dto.ProductDetailWrapperResponse;
import com.devcrew.moodcode.domain.product.dto.ProductListResponse;
import com.devcrew.moodcode.domain.product.dto.ProductOptionsResponse;
import com.devcrew.moodcode.domain.product.dto.ProductResponse;
import com.devcrew.moodcode.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 목록 조회
     * - category
     * - keyword
     */
    @GetMapping
    public ProductListResponse getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword
    ) {
        List<ProductResponse> products =
                productService.getProducts(category, keyword);

        return new ProductListResponse(products.size(), products);
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("/{productId}")
    public ProductDetailWrapperResponse getProductDetail(
            @PathVariable Long productId
    ) {
        ProductDetailResponse detail =
                productService.getProductDetail(productId);

        return new ProductDetailWrapperResponse(detail);
    }

    @GetMapping("/{productId}/option")
    public ResponseEntity<ProductOptionsResponse> getOption(
        @PathVariable Long productId
    ) {
        ProductOptionsResponse response =
            productService.getOption(productId);

        return ResponseEntity.ok(response);
    }
}
