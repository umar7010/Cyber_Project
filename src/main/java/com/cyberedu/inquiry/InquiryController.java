package com.cyberedu.inquiry;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inquiry")
public class InquiryController {
    private final InquiryRepository inquiryRepo;

    public InquiryController(InquiryRepository inquiryRepo) {
        this.inquiryRepo = inquiryRepo;
    }

    @PostMapping
    public Inquiry createInquiry(@RequestBody Inquiry inquiry) {
        return inquiryRepo.save(inquiry);
    }

    @GetMapping
    public List<Inquiry> getAllInquiries() {
        return inquiryRepo.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/status/{status}")
    public List<Inquiry> getInquiriesByStatus(@PathVariable InquiryStatus status) {
        return inquiryRepo.findByStatusOrderByCreatedAtDesc(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inquiry> getInquiryById(@PathVariable Long id) {
        Optional<Inquiry> inquiry = inquiryRepo.findById(id);
        return inquiry.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Inquiry> updateInquiryStatus(@PathVariable Long id, @RequestBody InquiryStatusUpdate statusUpdate) {
        Optional<Inquiry> optionalInquiry = inquiryRepo.findById(id);
        if (optionalInquiry.isPresent()) {
            Inquiry inquiry = optionalInquiry.get();
            inquiry.setStatus(statusUpdate.getStatus());
            if (statusUpdate.getAdminResponse() != null) {
                inquiry.setAdminResponse(statusUpdate.getAdminResponse());
                inquiry.setRespondedAt(java.time.LocalDateTime.now());
            }
            return ResponseEntity.ok(inquiryRepo.save(inquiry));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        if (inquiryRepo.existsById(id)) {
            inquiryRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // DTO for status updates
    public static class InquiryStatusUpdate {
        private InquiryStatus status;
        private String adminResponse;

        public InquiryStatus getStatus() { return status; }
        public void setStatus(InquiryStatus status) { this.status = status; }
        public String getAdminResponse() { return adminResponse; }
        public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }
    }
}
