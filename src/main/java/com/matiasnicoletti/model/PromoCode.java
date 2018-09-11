package com.matiasnicoletti.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "promo_codes")
public class PromoCode {

	@Id
	private String id;
	@NotNull
	private BigDecimal amount;
	@NotNull
	private Boolean isActive;
	@NotNull
	private String eventLatitude;
	@NotNull
	private String eventLongitude;
	private String eventName;
	private LocalDate expireDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getEventLatitude() {
		return eventLatitude;
	}

	public void setEventLatitude(String eventLatitude) {
		this.eventLatitude = eventLatitude;
	}

	public String getEventLongitude() {
		return eventLongitude;
	}

	public void setEventLongitude(String eventLongitude) {
		this.eventLongitude = eventLongitude;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public LocalDate getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
