package com.example.demo.handler;

import java.time.Instant;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.exception.base.BaseException;
import com.example.demo.exception.exceptions.AlreadyExistsException;
import com.example.demo.exception.exceptions.AuthException;
import com.example.demo.exception.exceptions.BadRequestException;
import com.example.demo.exception.exceptions.BusinessException;
import com.example.demo.exception.exceptions.DataImportException;
import com.example.demo.exception.exceptions.EntityException;
import com.example.demo.exception.exceptions.EntityNotFoundException;
import com.example.demo.exception.exceptions.ValidationException;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Hidden
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final MessageSource messageSource;

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<ApiErrorDto> handleAuthorizationDeniedException(
			AuthorizationDeniedException ex, HttpServletRequest request) {
		String message = messageSource.getMessage(
				"exception.authorization.denied",
				null,
				"exception.unexpected",
				LocaleContextHolder.getLocale());

		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(new ApiErrorDto(
						HttpStatus.FORBIDDEN.value(),
						message,
						Instant.now(),
						request.getRequestURI(),
						request.getMethod()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorDto> handleValidationExceptions(
			MethodArgumentNotValidException ex, WebRequest request) {
		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.toList();

		String message = messageSource.getMessage(
				"exception.validation.error",
				null,
				"exception.unexpected",
				LocaleContextHolder.getLocale());

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDto(
						HttpStatus.BAD_REQUEST.value(),
						message,
						Instant.now(),
						errors));
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ApiErrorDto> handleBaseException(BaseException ex, HttpServletRequest request) {
		String message = messageSource.getMessage(
				ex.getMessageKey(),
				ex.getArgs(),
				ex.getMessageKey(),
				ex.getLocale());

		HttpStatus status = determineHttpStatus(ex);

		return ResponseEntity
				.status(status)
				.body(new ApiErrorDto(
						status.value(),
						message,
						Instant.now(),
						request.getRequestURI(),
						request.getMethod()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorDto> handleGenericException(Exception ex, HttpServletRequest request) {
		String message = messageSource.getMessage(
				"exception.unexpected",
				null,
				"exception.unexpected",
				LocaleContextHolder.getLocale());

		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiErrorDto(
						HttpStatus.INTERNAL_SERVER_ERROR.value(),
						message,
						Instant.now(),
						request.getRequestURI(),
						request.getMethod()));
	}

	private HttpStatus determineHttpStatus(BaseException ex) {
		if (ex instanceof AuthException) {
			return HttpStatus.UNAUTHORIZED;
		} else if (ex instanceof EntityNotFoundException || ex instanceof EntityException) {
			return HttpStatus.NOT_FOUND;
		} else if (ex instanceof AlreadyExistsException) {
			return HttpStatus.CONFLICT;
		} else if (ex instanceof ValidationException) {
			return HttpStatus.BAD_REQUEST;
		} else if (ex instanceof BusinessException) {
			return HttpStatus.BAD_REQUEST;
		} else if (ex instanceof BadRequestException) {
			return HttpStatus.BAD_REQUEST;
		} else if (ex instanceof DataImportException) {
			return HttpStatus.BAD_REQUEST;
		}

		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
