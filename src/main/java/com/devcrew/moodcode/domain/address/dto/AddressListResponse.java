package com.devcrew.moodcode.domain.address.dto;

import com.devcrew.moodcode.domain.address.Address;
import lombok.Builder;

import java.util.List;

@Builder
public record AddressListResponse(
        List<AddressResponse> addresses,
        int totalCount
) {
    public static AddressListResponse from(List<Address> addresses) {
        return AddressListResponse.builder()
                .addresses(addresses.stream()
                        .map(AddressResponse::from)
                        .toList())
                .totalCount(addresses.size())
                .build();

    }
}
