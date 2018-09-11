package com.matiasnicoletti.exception;

public class PromoCodeInvalidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "The promo code is invalid";

	public PromoCodeInvalidException() {
		super(MESSAGE);
	}
}
