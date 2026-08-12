package com.smartlib.repository;

import com.smartlib.entity.SearchHistory;
import com.smartlib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop10ByUserOrderBySearchDateDesc(User user);
    List<SearchHistory> findByUserId(Long userId);
}
