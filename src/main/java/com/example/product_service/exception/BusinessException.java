package com.example.product_service.exception;

public class BusinessException extends RuntimeException {

    private final int errorCode;

    // Constructor 1: Chỉ truyền message (Mặc định mã lỗi là 400 - Bad Request)
    public BusinessException(String message) {
        super(message);
        this.errorCode = 400;
    }

    // Constructor 2: Truyền mã lỗi tùy chỉnh (ví dụ: 404, 409) kèm message
    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // Getter để ExceptionHandler có thể lấy được mã lỗi
    public int getErrorCode() {
        return errorCode;
    }
}