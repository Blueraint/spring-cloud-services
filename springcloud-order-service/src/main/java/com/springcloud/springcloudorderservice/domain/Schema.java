package com.springcloud.springcloudorderservice.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Schema {
    private String type;
    private List<Field> fields;
    private boolean optional;
    private String name;
}
