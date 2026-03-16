package com.devcrew.moodcode.domain.wishlist;

import com.devcrew.moodcode.domain.brand.Brand;
import com.devcrew.moodcode.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wishlist_brand")
public class WishlistBrand extends BaseTimeEntity {

  @Id
  @Column(name = "wishlist_brand_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "brand_id")
  private Brand brand;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wishlist_id")
  private Wishlist wishlist;

  public static WishlistBrand of(Brand brand, Wishlist wishlist) {
    return WishlistBrand.builder()
        .brand(brand)
        .wishlist(wishlist)
        .build();
  }
}

