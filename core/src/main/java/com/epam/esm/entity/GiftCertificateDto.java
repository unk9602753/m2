package com.epam.esm.entity;

import com.epam.esm.config.IdDeserializer;
import com.epam.esm.config.StringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDto {

    @JsonDeserialize(using = IdDeserializer.class)
    private Long id;
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;
    @JsonDeserialize(using = StringDeserializer.class)
    private String description;
    private double price;
    private int duration;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime createDate;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime lastUpdateDate;
    private List<Tag> tags;

    public GiftCertificateDto(GiftCertificate giftCertificate) {
        this.id = giftCertificate.getId();
        this.name = giftCertificate.getName();
        this.description = giftCertificate.getDescription();
        this.price = giftCertificate.getPrice();
        this.duration = giftCertificate.getDuration();
        this.createDate = giftCertificate.getCreateDate();
        this.lastUpdateDate = giftCertificate.getLastUpdateDate();
        this.tags = new ArrayList<>();
    }

    public GiftCertificateDto(String name, String description, double price,
                              int duration, LocalDateTime createDate,
                              LocalDateTime lastUpdateDate, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }
}
