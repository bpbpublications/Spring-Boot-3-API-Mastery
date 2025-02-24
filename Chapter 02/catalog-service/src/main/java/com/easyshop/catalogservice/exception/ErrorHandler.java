package com.easyshop.catalogservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public Mono<ResponseEntity<Object>> handle(
            ProductNotFoundException ex, ServerWebExchange serverWebExchange) {

        log.warn(ex.getMessage());
        var pd = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return Mono.just(new ResponseEntity<>(pd, HttpStatus.NOT_FOUND));

    }

    @ExceptionHandler(ProducAlreadyExistsException.class)
    public Mono<ResponseEntity<Object>> handle(
            ProducAlreadyExistsException ex, ServerWebExchange serverWebExchange) {

        log.warn(ex.getMessage());
        var pd = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return Mono.just(new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST));

    }

    @ExceptionHandler(ProductCodeMismatchException.class)
    public Mono<ResponseEntity<Object>> handle(
            ProductCodeMismatchException ex, ServerWebExchange serverWebExchange) {

        log.warn(ex.getMessage());
        var pd = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return Mono.just(new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST));

    }

    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        String message = buildValidationMessage(ex);
        log.warn(ex.getMessage());
        var pd = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        return Mono.just(ResponseEntity.badRequest().body(pd));
    }

    private String buildValidationMessage(WebExchangeBindException ex) {
        if(CollectionUtils.isEmpty(ex.getFieldErrors())) {
            return ex.getReason();
        }
        else {
            return ex.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
        }
    }

}
