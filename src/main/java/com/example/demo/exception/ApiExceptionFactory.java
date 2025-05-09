package com.example.demo.exception;

import org.springframework.stereotype.Component;

import com.example.demo.exception.exceptions.AlreadyExistsException;
import com.example.demo.exception.exceptions.AuthException;
import com.example.demo.exception.exceptions.BusinessException;
import com.example.demo.exception.exceptions.EntityException;
import com.example.demo.exception.exceptions.EntityNotFoundException;
import com.example.demo.exception.exceptions.ValidationException;
import com.example.demo.utils.LocaleUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ApiExceptionFactory {
	private final LocaleUtils localeUtils;

	public AuthException authException(String messageKey, Object... args) {
		return new AuthException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public EntityException entityNotFoundGeneric(String entityName, Object criteria) {
		return new EntityException("exception.entity.not.found.generic", localeUtils.getCurrentLocale(), entityName,
				criteria);
	}

	public EntityNotFoundException entityNotFound(String messageKey, Object... args) {
		return new EntityNotFoundException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public AlreadyExistsException conflictException(String messageKey, Object... args) {
		return new AlreadyExistsException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public ValidationException validationException(String messageKey, Object... args) {
		return new ValidationException(messageKey, localeUtils.getCurrentLocale(), args);
	}

	public BusinessException businessException(String messageKey, Object... args) {
		return new BusinessException(messageKey, localeUtils.getCurrentLocale(), args);
	}
}