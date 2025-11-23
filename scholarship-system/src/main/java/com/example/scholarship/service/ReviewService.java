package com.example.scholarship.service;

import com.example.scholarship.entity.Review;

/**
 * 评审业务服务接口
 * 
 * @author System
 * @version 1.0.0
 */
public interface ReviewService {
    
    /**
     * 提交奖学金申请
     * @param userId 用户ID
     * @param scholarshipTypeId 奖学金类型ID
     * @return 创建的评审记录
     * @throws Exception 当存在重复申请时抛出异常
     */
    Review submitApplication(Long userId, Long scholarshipTypeId) throws Exception;
}
