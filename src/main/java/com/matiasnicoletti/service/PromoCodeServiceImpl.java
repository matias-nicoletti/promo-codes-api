package com.matiasnicoletti.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.matiasnicoletti.config.CachingConfig;
import com.matiasnicoletti.model.PromoCode;
import com.matiasnicoletti.repository.PromoCodeRepository;
import com.matiasnicoletti.utils.GeoUtils;

@Component
public class PromoCodeServiceImpl implements PromoCodeService {

	@Autowired
	private PromoCodeRepository promoCodeRepository;

	@Value("${promocodes.radiusinmeters:2000}")
	private Long radiusInMeters;

	@Override
	@CacheEvict(value = CachingConfig.PROMO_CODES_CACHE, key = "#id")
	public Optional<PromoCode> updatePromoCode(String id, PromoCode newPromoCode) {
		return promoCodeRepository.findById(id).map(oldPromoCode -> {
			oldPromoCode.setIsActive(newPromoCode.getIsActive());
			oldPromoCode.setExpireDate(newPromoCode.getExpireDate());
			oldPromoCode.setEventName(newPromoCode.getEventName());
			oldPromoCode.setEventLatitude(newPromoCode.getEventLatitude());
			oldPromoCode.setEventLongitude(newPromoCode.getEventLongitude());
			return promoCodeRepository.save(oldPromoCode);
		});
	}

	@Override
	public PromoCode savePromoCode(PromoCode promoCode) {
		return promoCodeRepository.save(promoCode);
	}

	@Override
	@Cacheable(CachingConfig.PROMO_CODES_CACHE)
	public Optional<PromoCode> getPromoCode(String id) {
		return promoCodeRepository.findById(id);
	}

	@Override
	public List<PromoCode> getPromoCodes(Boolean isActive) {
		List<PromoCode> result = (Objects.isNull(isActive)) ? promoCodeRepository.findAll()
				: ((isActive) ? promoCodeRepository.findByIsActiveTrue() : promoCodeRepository.findByIsActiveFalse());
		return StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public Boolean isValid(PromoCode promoCode, String originLatitude, String originLongitude,
			String destinationLatitude, String destinationLongitude) {
		return promoCode.getIsActive() && checkExpirationDate(promoCode)
				&& (checkWithinRadius(promoCode, originLatitude, originLongitude)
						|| (checkWithinRadius(promoCode, destinationLatitude, destinationLongitude)));
	}

	private Boolean checkWithinRadius(PromoCode promoCode, String latitude, String longitude) {
		try {
			Double distanceInMeters = GeoUtils.distanceInMeters(promoCode.getEventLatitude(),
					promoCode.getEventLongitude(), latitude, longitude);
			return radiusInMeters >= distanceInMeters;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	private boolean checkExpirationDate(PromoCode promoCode) {
		return promoCode.getExpireDate().isAfter(LocalDate.now());
	}

}
