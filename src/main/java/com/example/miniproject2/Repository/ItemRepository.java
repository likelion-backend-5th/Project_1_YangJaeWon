package com.example.miniproject2.Repository;

import com.example.miniproject2.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //Page<Item> findByIdAndWriter(Long id, String writer, Pageable pageable);
}
