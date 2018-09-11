package com.matiasnicoletti.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.matiasnicoletti.controller.response.ErrorResponse;
import com.matiasnicoletti.exception.PromoCodeInvalidException;
import com.matiasnicoletti.exception.PromoCodeNotFoundException;

@ControllerAdvice
public class PromoCodeControllerAdvice {

	@ResponseBody
	@ExceptionHandler(PromoCodeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ErrorResponse promoCodetNotFoundHandler(PromoCodeNotFoundException ex) {
		return new ErrorResponse(ex.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(PromoCodeInvalidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ErrorResponse promoCodeInvalidHandler(PromoCodeInvalidException ex) {
		return new ErrorResponse(ex.getMessage());
	}

}
