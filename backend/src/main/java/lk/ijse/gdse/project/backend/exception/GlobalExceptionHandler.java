package lk.ijse.gdse.project.backend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lk.ijse.gdse.project.backend.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new APIResponse(404, "User Not Found", e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse handleBadCredentialsException(BadCredentialsException e) {
        return new APIResponse(404, "Bad Credentials", e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse handleJWTTokenExpiredJwtException(ExpiredJwtException e) {
        return new APIResponse(401, "Jwt Token Expired", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse handleRuntimeException(RuntimeException e) {
        return new APIResponse(500, "Internal Server Error", e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponse handleAccessDenied(AccessDeniedException e) {
        return new APIResponse(403, "Access Denied", e.getMessage());
    }
}
