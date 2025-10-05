package com.assecor.assessment.repository;

import com.assecor.assessment.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Person entities.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface PersonJpaRepository extends JpaRepository<Person, Long> {
    
    /**
     * Find persons by color name (case-insensitive).
     * 
     * @param colorName the color name to search for
     * @return list of persons with the specified color
     */
    @Query("SELECT p FROM Person p WHERE LOWER(p.color.name) = LOWER(:colorName)")
    List<Person> findByColorNameIgnoreCase(@Param("colorName") String colorName);
}
