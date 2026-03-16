package com.devcrew.moodcode.domain.address.dto;

import com.devcrew.moodcode.domain.address.Address;
import lombok.Builder;
/**
 * response를 설계할 때에는 프론트단으로 보내줄 정보만 골라서 담아주면 된다.
 * 단, 엔티티를 그대로 반환했을 때 순환 참조 문제 혹은 불필요한 정보도 함께 노출 될 수 있기 때문에
 record를 활용해서 깔끔하게 구현해 줄 수 있다.
 * @param id
 * @param recipientName
 * @param phoneNumber
 * @param roadAddress
 * @param detailAddress
 * @param isDefault
 */
@Builder
public record AddressResponse(
        Long id,
        String recipientName,
        String phoneNumber,
        String roadAddress,
        String detailAddress,
        boolean isDefault
) {
    public static AddressResponse from(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .recipientName(address.getRecipientName())
                .phoneNumber(address.getPhoneNumber())
                .roadAddress(address.getRoadAddress())
                .detailAddress(address.getDetailAddress())
                .isDefault(address.isDefault())
                .build();
    }
}