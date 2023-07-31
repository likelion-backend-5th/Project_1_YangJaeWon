package com.example.miniproject2.Repository;

import com.example.miniproject2.Entity.Negotiation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    Page<Negotiation> findByItemId(Long itemId, Pageable pageable);
    Page<Negotiation> findByItemIdAndWriter(Long itemId, String writer, Pageable pageable);
}

