package com.matiasnicoletti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matiasnicoletti.model.PromoCode;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {

	List<PromoCode> findByIsActiveTrue();

	List<PromoCode> findByIsActiveFalse();

}
