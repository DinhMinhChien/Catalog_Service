package com.example.product_service.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource ;
    private String getMessage(String key) {
        return messageSource.getMessage(
                key,
                null,
                "System error , please try again",
                LocaleContextHolder.getLocale()
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = getMessage("MethodArgumentNotValidException.message") ;
        List<String> systemMessage = exception
                .getBindingResult()
                .getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList()) ;
        int code = 400 ;
        ErrorResponse errorResponse = new ErrorResponse(message,systemMessage,code) ;
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleBusinessException(BusinessException exception) {
        String message = getMessage("BusinessException.message");
        List<String> systemMessage = exception.getMessage().lines().toList();

        // 1. Lấy mã code động từ exception (ví dụ: 409, 404)
        int code = exception.getErrorCode();

        ErrorResponse errorResponse = new ErrorResponse(message, systemMessage, code);

        // 2. Chuyển đổi số int sang HttpStatus (nếu truyền số tào lao thì mặc định về 400)
        HttpStatus httpStatus = HttpStatus.resolve(code);
        if (httpStatus == null) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        // 3. Trả về response với status động
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {
        String message = getMessage("Exception.message") ;
        List<String> systemMessage = exception.getMessage().lines().toList() ;
        int code  = 500 ;
        ErrorResponse errorResponse = new ErrorResponse(message,systemMessage,code) ;
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR) ;
    }
}
