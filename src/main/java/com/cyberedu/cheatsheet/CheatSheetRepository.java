package com.cyberedu.cheatsheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CheatSheetRepository extends JpaRepository<CheatSheet, Long> {
    List<CheatSheet> findByIsPublishedTrueOrderByCreatedAtDesc();
    List<CheatSheet> findByCategoryOrderByCreatedAtDesc(String category);
    List<CheatSheet> findByTitleContainingIgnoreCase(String title);
}
