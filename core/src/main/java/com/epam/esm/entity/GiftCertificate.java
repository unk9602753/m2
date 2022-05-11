package com.epam.esm.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class GiftCertificate {
    private long id;
    private String name;
    private String description;
    private double price;
    private int duration;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime createDate;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime lastUpdateDate;

    public GiftCertificate(GiftCertificateDto giftCertificateDto) {
        this.id = giftCertificateDto.getId();
        this.name = giftCertificateDto.getName();
        this.description = giftCertificateDto.getDescription();
        this.price = giftCertificateDto.getPrice();
        this.duration = giftCertificateDto.getDuration();
        this.createDate = giftCertificateDto.getCreateDate();
        this.lastUpdateDate = giftCertificateDto.getLastUpdateDate();
    }
}
