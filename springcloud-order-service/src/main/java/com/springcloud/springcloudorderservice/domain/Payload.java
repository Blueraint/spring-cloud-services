package com.springcloud.springcloudorderservice.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Payload {
    private String order_id;
    private String user_id;
    private String product_id;
    private int qty;
    private int total_price;
    private int unit_price;
}
