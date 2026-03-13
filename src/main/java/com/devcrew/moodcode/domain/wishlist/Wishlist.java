package com.devcrew.moodcode.domain.wishlist;

import com.devcrew.moodcode.domain.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Wishlist {
  @Id @Column(name = "wishlist_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WishlistProduct> wishlistProducts = new ArrayList<>();

  @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WishlistBrand> wishlistBrands = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

}
