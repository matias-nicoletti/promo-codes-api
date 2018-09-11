package com.matiasnicoletti.service;

import java.util.List;
import java.util.Optional;

import com.matiasnicoletti.model.PromoCode;

public interface PromoCodeService {

	Optional<PromoCode> updatePromoCode(String id, PromoCode promoCode);

	PromoCode savePromoCode(PromoCode promoCode);

	Optional<PromoCode> getPromoCode(String id);

	List<PromoCode> getPromoCodes(Boolean isActive);

	Boolean isValid(PromoCode promoCode, String originLatitude, String originLongitude, String destinationLatitude,
			String destinationLongitude);

}
