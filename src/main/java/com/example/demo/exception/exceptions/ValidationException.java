package com.example.demo.exception.exceptions;

import java.util.Locale;

import com.example.demo.exception.base.BaseException;

public class ValidationException extends BaseException {
	public ValidationException(String messagekey, Locale locale, Object... args) {
		super(messagekey, locale, args);
	}
}
