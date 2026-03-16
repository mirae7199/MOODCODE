package com.devcrew.moodcode.domain.order.service;

import com.devcrew.moodcode.domain.address.Address;
import com.devcrew.moodcode.domain.address.repository.AddressRepository;
import com.devcrew.moodcode.domain.cart.CartItem;
import com.devcrew.moodcode.domain.cart.repository.CartItemRepository;
import com.devcrew.moodcode.domain.order.OrderStatus;
import com.devcrew.moodcode.domain.order.OrderedProduct;
import com.devcrew.moodcode.domain.order.OrderedProductStatus;
import com.devcrew.moodcode.domain.order.Orders;
import com.devcrew.moodcode.domain.order.controller.command.RegisterCartItemOrderCommand;
import com.devcrew.moodcode.domain.order.controller.command.RegisterProductCommand;
import com.devcrew.moodcode.domain.order.dto.FindOrderDetailResponse;
import com.devcrew.moodcode.domain.order.dto.FindOrdersResponse;
import com.devcrew.moodcode.domain.order.repository.OrderedProductRepository;
import com.devcrew.moodcode.domain.order.repository.OrdersRepository;
import com.devcrew.moodcode.domain.product.ProductOption;
import com.devcrew.moodcode.domain.product.repository.ProductOptionRepository;
import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.domain.user.exception.UserNotFoundException;
import com.devcrew.moodcode.domain.user.repository.UserRepository;
import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
  private final CartItemRepository cartItemRepository;
  private final OrdersRepository ordersRepository;
  private final AddressRepository addressRepository;
  private final UserRepository userRepository;
  private final OrderedProductRepository orderedProductRepository;
  private final ProductOptionRepository productOptionRepository;

  /**
   * @param userId
   * @param command
   * @return orderNo
   * 장바구니에서 주문 생성
   */
  @Transactional(readOnly = false)
  public String createCartItemOrder(Long userId, RegisterCartItemOrderCommand command) {
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    // 장바구니에 담겨 있는 상품 중 '사용자가 선택한' 상품 가져오기.
    List<CartItem> reqCartItems = cartItemRepository.findByIdInAndCart_User_Id(command.cartItemIds(), userId);

    // 총가격 = 상품 가격 * 갯수
    int totalPrice = reqCartItems.stream()
        .mapToInt(cartItem ->
            cartItem.getProductOption().getProduct().getOriginalPrice() * cartItem.getCount()
            ).sum();

    Address address;

    if (command.addressId() != null) {
      // 사용자가 직접 배송지를 선택한 경우
      address = addressRepository.findById(command.addressId())
          .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));
    } else {
      // 사용자가 배송지를 선택하지 않은 경우 (기본 배송지)
      address = addressRepository.findByUserAndIsDefaultTrue(user)
          .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));
    }

    Orders orders =
        Orders.of(
        UUID.randomUUID().toString(),
        address.getRecipientName(),
        address.getPhoneNumber(),
        address.getRoadAddress() + " " + address.getDetailAddress(),
        totalPrice,
        OrderStatus.PENDING,
        user
        );

    List<OrderedProduct> orderedProducts =
        reqCartItems.stream()
                .map(cartItem ->
                    OrderedProduct.of(
                        cartItem.getCount(),
                        cartItem.getProductOption().getProduct().getOriginalPrice(),
                        cartItem.getProductOption().getProduct().getName(),
                        cartItem.getProductOption().getName(),
                        OrderedProductStatus.PREPARING,
                        orders,
                        cartItem.getProductOption()
                    )
                ).toList();

    ordersRepository.save(orders);
    orderedProductRepository.saveAll(orderedProducts);

    return orders.getOrderNo();

  }

  /**
   * @param userId
   * @param command
   * @return orderNo
   * 상품 바로 주문 생성(상품 페이지에서 주문)
   */
  @Transactional(readOnly = false)
  public String createProductOrder(Long userId, RegisterProductCommand command) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    ProductOption productOption = productOptionRepository.findById(command.productOptionId())
        .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));

    // 총 주문 가격
    int totalPrice = productOption.getProduct().getOriginalPrice() * command.count();

    Address address;

    if (command.addressId() != null) {
      // 사용자가 직접 배송지를 선택한 경우
      address = addressRepository.findById(command.addressId())
          .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));
    } else {
      // 사용자가 배송지를 선택하지 않은 경우 (기본 배송지)
      address = addressRepository.findByUserAndIsDefaultTrue(user)
          .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));
    }

    Orders orders = Orders.of(
        UUID.randomUUID().toString(),
        address.getRecipientName(),
        address.getPhoneNumber(),
        address.getRoadAddress() + " " + address.getDetailAddress(),
        totalPrice,
        OrderStatus.PENDING,
        user
    );

    OrderedProduct orderedProduct = OrderedProduct.of(
        command.count(),
        productOption.getProduct().getOriginalPrice(),
        productOption.getProduct().getName(),
        productOption.getName(),
        OrderedProductStatus.PREPARING,
        orders,
        productOption
    );

    ordersRepository.save(orders);
    orderedProductRepository.save(orderedProduct);

    return orders.getOrderNo();
  }

  /**
   * @param userId
   * @return FindOrdersResponse
   * 사용자의 주문 목록 조회
   */
  public FindOrdersResponse getOrders(Long userId) {
    // 사용자가 주문한 주문 목록
    List<Orders> orders = ordersRepository.findByUser_id(userId);

    return FindOrdersResponse.from(orders);
  }

  /**
   * @param userId
   * @param orderNo
   * @return FindOrderDetailResponse
   * 사용자의 주문 목록 상세 조회
   */
  public FindOrderDetailResponse getOrdersDetail(Long userId, String orderNo) {
    // 주문 상세 조회
    Orders orders = ordersRepository.findByUser_idAndOrderNo(userId, orderNo)
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDERS_NOT_FOUND));

    return FindOrderDetailResponse.from(orders);

  }

}
