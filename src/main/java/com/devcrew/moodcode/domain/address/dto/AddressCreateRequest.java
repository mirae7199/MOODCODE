package com.devcrew.moodcode.domain.address.dto;


import com.devcrew.moodcode.domain.address.service.command.AddressCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record AddressCreateRequest(
        @NotBlank String recipientName,
        @NotBlank @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$") String phoneNumber,
        @NotBlank String roadAddress,
        @NotBlank String detailAddress,
        boolean isDefault
) {
    public AddressCreateCommand toCommand() {
        return AddressCreateCommand.builder()
                .recipientName(recipientName)
                .phoneNumber(phoneNumber)
                .roadAddress(roadAddress)
                .detailAddress(detailAddress)
                .isDefault(isDefault)
                .build();
    }
}