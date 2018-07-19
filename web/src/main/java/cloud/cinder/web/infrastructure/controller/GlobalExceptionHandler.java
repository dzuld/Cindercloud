package cloud.cinder.web.infrastructure.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String databaseError() {
        return "redirect:/wallet/login";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String authenticationException() {
        return "redirect:/wallet/login";
    }
}
