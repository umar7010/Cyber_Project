package com.cyberedu.inquiry;

public enum InquiryStatus {
    PENDING(0),
    RESPONDED(1),
    CLOSED(2),
    SPAM(3);
    
    private final int value;
    
    InquiryStatus(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static InquiryStatus fromValue(int value) {
        for (InquiryStatus status : InquiryStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid InquiryStatus value: " + value);
    }
}
