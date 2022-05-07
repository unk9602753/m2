package com.epam.esm.entity;

import com.epam.esm.config.IdDeserializer;
import com.epam.esm.config.StringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @JsonDeserialize(using = IdDeserializer.class)
    private long id;
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;
}
