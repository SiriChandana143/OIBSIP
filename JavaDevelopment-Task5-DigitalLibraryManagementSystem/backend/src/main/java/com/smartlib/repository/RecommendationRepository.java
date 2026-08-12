package com.smartlib.repository;

import com.smartlib.entity.Recommendation;
import com.smartlib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserOrderByRecommendationScoreDesc(User user);
    List<Recommendation> findTop10ByUserOrderByRecommendationScoreDesc(User user);
    void deleteByUser(User user);
}
