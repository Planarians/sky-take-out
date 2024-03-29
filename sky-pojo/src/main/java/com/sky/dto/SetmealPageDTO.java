package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealPageDTO {
    private Integer page = 1;
    private Integer pageSize = 5;
    private String name; //套餐名称
    private Long categoryId; //分类Id
    private Integer status; //套餐状态


}
