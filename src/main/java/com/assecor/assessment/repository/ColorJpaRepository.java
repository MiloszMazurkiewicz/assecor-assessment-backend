package com.assecor.assessment.repository;

import com.assecor.assessment.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorJpaRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByNameIgnoreCase(String name);
}
