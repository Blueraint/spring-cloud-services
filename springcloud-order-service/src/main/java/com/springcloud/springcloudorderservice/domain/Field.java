package com.springcloud.springcloudorderservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Field {
    private String type;
    private boolean optional;
    private String field;
}
