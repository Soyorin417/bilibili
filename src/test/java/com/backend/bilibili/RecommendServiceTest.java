package com.backend.bilibili;

import com.backend.bilibili.service.recommend.RecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RecommendServiceTest {

    @Autowired
    private RecommendService recommendService;

    @Test
    public void testRecommendTagsForUser() {
        Long userId = 1L;
        int topN = 5;

        List<Long> tags = recommendService.recommendTagsForUser(userId, topN);
        System.out.println("推荐结果: " + tags);
    }
}
