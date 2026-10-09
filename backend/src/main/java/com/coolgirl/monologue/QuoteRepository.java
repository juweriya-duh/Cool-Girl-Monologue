package com.coolgirl.monologue;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> findByStatusOrderByCreatedAtDesc(String status);
    List<Quote> findAllByOrderByCreatedAtDesc();
}
