package com.kcd.pos.product.repository;

import com.kcd.pos.product.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionRepsitory extends JpaRepository<Option, Long> {

    Optional<Option> findByOptionIdAndDeleteYn(Long optionId, String deleteYn);

}
