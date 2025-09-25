package com.cyberedu.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByStatus(InquiryStatus status);
    List<Inquiry> findByStatusOrderByCreatedAtDesc(InquiryStatus status);
    List<Inquiry> findByEmailOrderByCreatedAtDesc(String email);
    List<Inquiry> findAllByOrderByCreatedAtDesc();
}
