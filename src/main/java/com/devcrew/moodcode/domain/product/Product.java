package com.devcrew.moodcode.domain.product;

import com.devcrew.moodcode.domain.brand.Brand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category; // TOP, OUTER ...

    @Column(name = "original_price", nullable = false)
    private Integer originalPrice;

    @Column(name = "thumbnail_image_url")
    private String thumbnailImageUrl;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ProductOption> productOptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
