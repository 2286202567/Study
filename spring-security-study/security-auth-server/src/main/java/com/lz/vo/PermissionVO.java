package com.lz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVO {
    private Integer  id;
    private String name;
    private String resource;
    private Integer userId;
    private String expression;
}
