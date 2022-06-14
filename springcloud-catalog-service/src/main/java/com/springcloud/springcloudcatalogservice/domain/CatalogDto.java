package com.springcloud.springcloudcatalogservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogDto implements Serializable {
    private String productId;
    private String productName;
    private Integer qty;
    private Integer stock;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
