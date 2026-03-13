package com.devcrew.moodcode.domain.brand;

import com.devcrew.moodcode.domain.product.Product;
import com.devcrew.moodcode.domain.wishlist.WishlistBrand;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Brand {

  @Id @Column(name = "brand_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "brand_name")
  private String name;

  @Column(name = "brand_like_count")
  private int likeCount;

  @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WishlistBrand> wishlistBrands = new ArrayList<>();

  @OneToOne(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
  private Product product;

}
