package com.matiasnicoletti.controller.response;

import com.matiasnicoletti.model.PromoCode;

public class PromoCodeValidityResponse {

	private PromoCode promoCode;
	private Boolean isValid;

	public PromoCodeValidityResponse(PromoCode promoCode, Boolean isValid) {
		super();
		this.promoCode = promoCode;
		this.isValid = isValid;
	}

	public PromoCode getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(PromoCode promoCode) {
		this.promoCode = promoCode;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

}
