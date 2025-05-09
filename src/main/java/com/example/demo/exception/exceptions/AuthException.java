package com.example.demo.exception.exceptions;

import java.util.Locale;

import com.example.demo.exception.base.BaseException;

public class AuthException extends BaseException {
	public AuthException(String messageKey, Locale locale, Object... args) {
		super(messageKey, locale, args);
	}
}
