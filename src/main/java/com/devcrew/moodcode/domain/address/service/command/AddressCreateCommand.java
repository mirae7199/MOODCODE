package com.devcrew.moodcode.domain.address.service.command;

import com.devcrew.moodcode.domain.address.Address;
import com.devcrew.moodcode.domain.user.User;
import lombok.Builder;

@Builder
public record AddressCreateCommand(
        String recipientName,
        String phoneNumber,
        String roadAddress,
        String detailAddress,
        boolean isDefault
) {
    public Address toEntity(User user) {
        return Address.builder()
                .user(user)
                .recipientName(recipientName)
                .phoneNumber(phoneNumber)
                .roadAddress(roadAddress)
                .detailAddress(detailAddress)
                .isDefault(isDefault)
                .build();
    }
}
