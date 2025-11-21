package com.example.scholarship.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 评审申请请求的数据传输对象
 */
@Data
public class ReviewRequestDto {
    
    @NotNull(message = "奖学金类型ID不能为空")
    private Long scholarshipTypeId;
}