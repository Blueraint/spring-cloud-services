package com.springcloud.springcloudorderservice.dto;

import com.springcloud.springcloudorderservice.domain.Payload;
import com.springcloud.springcloudorderservice.domain.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class KafkaOrderDto implements Serializable {
    private Schema schema;
    private Payload payload;
}
