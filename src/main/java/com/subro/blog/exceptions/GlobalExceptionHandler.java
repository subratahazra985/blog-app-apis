package com.subro.blog.exceptions;

import com.subro.blog.payloads.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles ResourceNotFoundException and returns a response entity with an error message.
     *
     * @param ex the ResourceNotFoundException thrown when a requested resource is not found
     * @return a ResponseEntity containing an ApiResponse with the error message and an HTTP status of NOT_FOUND
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse=new ApiResponse(message,false);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a response entity containing a map of errors.
     * The keys of the map are the field names and the values are the corresponding error messages.
     *
     * @param ex the MethodArgumentNotValidException thrown when the request body is invalid
     * @return a ResponseEntity containing a map of errors and an HTTP status of BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        Map<String, String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return new ResponseEntity<>(Map.of(
                "error", "Invalid username or password",
                "status", HttpStatus.UNAUTHORIZED.value()
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UsernameNotFoundException ex) {
        return new ResponseEntity<>(Map.of(
                "error", ex.getMessage(),
                "status", HttpStatus.NOT_FOUND.value()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwt(ExpiredJwtException ex) {
        return new ResponseEntity<>(Map.of(
                "error", "JWT token expired",
                "status", HttpStatus.UNAUTHORIZED.value()
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwt(MalformedJwtException ex) {
        return new ResponseEntity<>(Map.of(
                "error", "Invalid JWT token",
                "status", HttpStatus.BAD_REQUEST.value()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(Map.of(
                "error", ex.getMessage(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
