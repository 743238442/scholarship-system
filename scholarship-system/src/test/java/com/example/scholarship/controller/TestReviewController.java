package com.example.scholarship.controller;

import com.example.scholarship.dto.ReviewDto;
import com.example.scholarship.entity.Review;
import com.example.scholarship.entity.ReviewStatus;
import com.example.scholarship.entity.ScholarshipType;
import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.User;
import com.example.scholarship.repository.ReviewRepository;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Arrays;
import java.util.List;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ReviewController的JUnit测试类
 * 测试my-applications接口功能
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TestReviewController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private UserRepository userRepository;

    private User mockUser;
    private Student mockStudent;
    private ScholarshipType mockScholarshipType;
    private Review mockReview;

    @BeforeEach
    void setUp() {
        // 1. 创建模拟用户
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testStudent");
        mockUser.setRealName("测试学生");

        // 2. 创建模拟学生
        mockStudent = new Student();
        mockStudent.setId(1L);
        mockStudent.setStudentNo("2020001");
        mockStudent.setName("测试学生");
        mockStudent.setUser(mockUser);

        // 3. 创建模拟奖学金类型
        mockScholarshipType = new ScholarshipType();
        mockScholarshipType.setId(1L);
        mockScholarshipType.setName("国家奖学金");
        mockScholarshipType.setAmount(new BigDecimal(8000));

        // 4. 创建模拟评审记录
        mockReview = new Review();
        mockReview.setId(1L);
        mockReview.setStudent(mockStudent);
        mockReview.setScholarshipType(mockScholarshipType);
        mockReview.setReviewStatus(ReviewStatus.PENDING);
        mockReview.setReviewerId(1L);
        mockReview.setCreatedAt(LocalDateTime.now());

        // 5. 设置Spring Security的认证上下文，包含STUDENT角色
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_STUDENT"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "testStudent", null, authorities);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // 6. 设置Mock对象的行为
        when(userRepository.findByUsername("testStudent")).thenReturn(Optional.of(mockUser));
        when(studentRepository.findByUser(mockUser)).thenReturn(Optional.of(mockStudent));
    }

    @Test
    void testGetMyApplications_Success() throws Exception {
        // 准备测试数据
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(mockReview);
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviewPage = new PageImpl<>(reviewList, pageable, reviewList.size());

        // 设置Mock对象的行为
        when(reviewRepository.findByStudentIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class)))
                .thenReturn(reviewPage);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/my-applications"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].scholarshipTypeName").value("国家奖学金"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1));
    }
}