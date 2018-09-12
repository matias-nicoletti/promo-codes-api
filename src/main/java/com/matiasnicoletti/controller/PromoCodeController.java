package com.matiasnicoletti.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matiasnicoletti.controller.response.PromoCodeValidityResponse;
import com.matiasnicoletti.exception.PromoCodeInvalidException;
import com.matiasnicoletti.exception.PromoCodeNotFoundException;
import com.matiasnicoletti.model.PromoCode;
import com.matiasnicoletti.service.PromoCodeService;

@RestController
public class PromoCodeController {

	@Autowired
	private PromoCodeService promoCodeService;

	@GetMapping("/promo-codes")
	public List<PromoCode> getPromoCodes(@RequestParam(required = false) Boolean isActive) {
		return promoCodeService.getPromoCodes(isActive);
	}

	@GetMapping("/promo-codes/{id}")
	public PromoCode getPromoCode(@PathVariable String id) {
		return promoCodeService.getPromoCode(id).orElseThrow(PromoCodeNotFoundException::new);
	}

	@PostMapping("/promo-codes")
	public PromoCode createPost(@Valid @RequestBody PromoCode promoCode) {
		return promoCodeService.savePromoCode(promoCode);
	}

	@PutMapping("/promo-codes/{id}")
	public PromoCode updatePromoCode(@PathVariable String id, @Valid @RequestBody PromoCode promoCode) {
		return promoCodeService.updatePromoCode(id, promoCode).orElseThrow(PromoCodeNotFoundException::new);
	}

	@GetMapping("/promo-codes/{id}/validity")
	public PromoCodeValidityResponse getPromoCodeValidity(@PathVariable String id, @RequestParam String originLatitude,
			@RequestParam String originLongitude, @RequestParam String destinationLatitude,
			@RequestParam String destinationLongitude) {
		PromoCode promoCode = this.getPromoCode(id);
		Boolean isValid = promoCodeService.isValid(promoCode, originLatitude, originLongitude, destinationLatitude,
				destinationLongitude);
		if (!isValid) {
			throw new PromoCodeInvalidException();
		}
		return new PromoCodeValidityResponse(promoCode, isValid);
	}

}
