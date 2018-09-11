package com.matiasnicoletti.exception;

public class PromoCodeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "The promo code does not exists";

	public PromoCodeNotFoundException() {
		super(MESSAGE);
	}

}
