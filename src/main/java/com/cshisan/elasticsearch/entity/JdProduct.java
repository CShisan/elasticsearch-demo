package com.cshisan.elasticsearch.entity;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author CShisan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JdProduct extends BaseEntity{
    private Long sku;
    private String shop;
    private BigDecimal price;
    private String name;
    private String img;
}
