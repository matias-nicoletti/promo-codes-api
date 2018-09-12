package com.matiasnicoletti.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.matiasnicoletti.model.PromoCode;
import com.matiasnicoletti.repository.PromoCodeRepository;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ PromoCodeServiceImpl.class })
public class PromoCodeServiceImplTest {

	private static String DEFAULT_LATITUDE = "41.391241";
	private static String DEFAULT_LONGITUDE = "2.180644";
	private static String NEAR_LATITUDE = "41.385528";
	private static String NEAR_LONGITUDE = "2.185802";
	private static String FAR_LATITUDE = "42.385528";
	private static String FAR_LONGITUDE = "2.185802";
	private static Long RADIUS = 2000l;

	@InjectMocks
	private PromoCodeServiceImpl promoCodeServiceImpl;

	@Mock
	private PromoCodeRepository promoCodeRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUpdatePromoCode() {
		String id = "promocode1";
		PromoCode promoCode = new PromoCode("promocode1", BigDecimal.valueOf(5), true, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.now());
		PromoCode updated = new PromoCode("promocode1", BigDecimal.valueOf(10), false, "0", "0", "Arc de Triomf++",
				LocalDate.parse("2018-12-01"));
		Optional<PromoCode> response = Optional.ofNullable(promoCode);
		when(promoCodeRepository.findById(updated.getId())).thenReturn(response);
		when(promoCodeRepository.save(any(PromoCode.class))).thenReturn(updated);
		Optional<PromoCode> returned = promoCodeServiceImpl.updatePromoCode(id, updated);

		verify(promoCodeRepository, times(1)).findById(id);
		verify(promoCodeRepository, times(1)).save(any(PromoCode.class));
		verifyNoMoreInteractions(promoCodeRepository);
		assertPromoCode(updated, returned.get());
	}

	@Test
	public void testSavePromoCode() {
		PromoCode promoCode = new PromoCode("promocode1", BigDecimal.valueOf(5), true, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.parse("2018-12-31"));
		PromoCode persisted = new PromoCode("promocode1", BigDecimal.valueOf(5), true, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.parse("2018-12-31"));
		when(promoCodeRepository.save(any(PromoCode.class))).thenReturn(persisted);
		PromoCode returned = promoCodeServiceImpl.savePromoCode(promoCode);

		ArgumentCaptor<PromoCode> promoCodeArgument = ArgumentCaptor.forClass(PromoCode.class);
		verify(promoCodeRepository, times(1)).save(promoCodeArgument.capture());
		verifyNoMoreInteractions(promoCodeRepository);
		assertPromoCode(promoCode, promoCodeArgument.getValue());
		assertEquals(persisted, returned);

	}

	private void assertPromoCode(PromoCode expected, PromoCode actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getAmount(), actual.getAmount());
		assertEquals(expected.getEventLatitude(), actual.getEventLatitude());
		assertEquals(expected.getEventLongitude(), actual.getEventLongitude());
		assertEquals(expected.getEventName(), actual.getEventName());
		assertEquals(expected.getExpireDate(), actual.getExpireDate());
		assertEquals(expected.getIsActive(), actual.getIsActive());
	}

	@Test
	public void testGetPromoCode() {
		String id = "promocode1";
		PromoCode promoCode = new PromoCode("promocode1", BigDecimal.valueOf(5), true, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.now());
		Optional<PromoCode> response = Optional.ofNullable(promoCode);
		when(promoCodeRepository.findById(id)).thenReturn(response);

		PromoCode returned = promoCodeServiceImpl.getPromoCode(id).get();
		verify(promoCodeRepository, times(1)).findById(id);
		verifyNoMoreInteractions(promoCodeRepository);
		assertEquals(promoCode, returned);
	}

	@Test
	public void testGetPromoCodes() {
		List<PromoCode> promoCodes = new ArrayList<>();
		PromoCode promoCode1 = new PromoCode("promocode1", BigDecimal.valueOf(5), true, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.now());
		PromoCode promoCode2 = new PromoCode("promocode2", BigDecimal.valueOf(5), false, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.parse("2018-12-31"));
		Collections.addAll(promoCodes, promoCode1, promoCode2);
		when(promoCodeRepository.findAll()).thenReturn(promoCodes);
		List<PromoCode> returned = promoCodeServiceImpl.getPromoCodes(null);

		verify(promoCodeRepository, times(1)).findAll();
		verifyNoMoreInteractions(promoCodeRepository);
		assertEquals(promoCodes, returned);
	}

	@Test
	public void testIsValidOK() {
		PromoCode promoCodeActive = new PromoCode("promocode1", BigDecimal.valueOf(5), true, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.now().plusDays(1));
		PromoCode promoCodeInactive = new PromoCode("promocode1", BigDecimal.valueOf(5), false, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.now().plusDays(1));
		PromoCode promoCodeExpired = new PromoCode("promocode1", BigDecimal.valueOf(5), true, DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE, "Arc de Triomf", LocalDate.now().minusDays(1));

		ReflectionTestUtils.setField(promoCodeServiceImpl, "radiusInMeters", Long.valueOf(RADIUS));
		// true if active, not expired, and origin or destination near
		assertTrue(promoCodeServiceImpl.isValid(promoCodeActive, NEAR_LATITUDE, NEAR_LONGITUDE, NEAR_LATITUDE,
				NEAR_LONGITUDE));
		assertTrue(promoCodeServiceImpl.isValid(promoCodeActive, NEAR_LATITUDE, NEAR_LONGITUDE, FAR_LATITUDE,
				FAR_LONGITUDE));
		assertTrue(promoCodeServiceImpl.isValid(promoCodeActive, FAR_LATITUDE, FAR_LONGITUDE, NEAR_LATITUDE,
				NEAR_LONGITUDE));

		// false if inactive, expired or far
		assertFalse(promoCodeServiceImpl.isValid(promoCodeInactive, NEAR_LATITUDE, NEAR_LONGITUDE, NEAR_LATITUDE,
				NEAR_LONGITUDE));
		assertFalse(promoCodeServiceImpl.isValid(promoCodeExpired, NEAR_LATITUDE, NEAR_LONGITUDE, NEAR_LATITUDE,
				NEAR_LONGITUDE));
		assertFalse(
				promoCodeServiceImpl.isValid(promoCodeActive, FAR_LATITUDE, FAR_LATITUDE, FAR_LATITUDE, FAR_LATITUDE));

	}

}
