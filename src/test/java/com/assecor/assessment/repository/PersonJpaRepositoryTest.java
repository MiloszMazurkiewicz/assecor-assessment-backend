package com.assecor.assessment.repository;

import com.assecor.assessment.AbstractIntegrationTest;
import com.assecor.assessment.model.Person;
import com.assecor.assessment.model.Color;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonJpaRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonJpaRepository personJpaRepository;
    
    @Autowired
    private ColorJpaRepository colorJpaRepository;

    @Test
    void findByColorNameIgnoreCase_ShouldReturnPersonsWithMatchingColor() {
        List<Person> bluePersons = personJpaRepository.findByColorNameIgnoreCase("blau");
        assertNotNull(bluePersons);
        assertEquals(2, bluePersons.size()); // Hans and Bertram
        assertTrue(bluePersons.stream().anyMatch(p -> p.getName().equals("Hans")));
        assertTrue(bluePersons.stream().anyMatch(p -> p.getName().equals("Bertram")));

        List<Person> greenPersons = personJpaRepository.findByColorNameIgnoreCase("grün");
        assertNotNull(greenPersons);
        assertEquals(3, greenPersons.size()); // Peter, Anders, and Klaus
        assertTrue(greenPersons.stream().anyMatch(p -> p.getName().equals("Peter")));
        assertTrue(greenPersons.stream().anyMatch(p -> p.getName().equals("Anders")));
        assertTrue(greenPersons.stream().anyMatch(p -> p.getName().equals("Klaus")));
    }

    @Test
    void findById_WhenPersonExists_ShouldReturnPerson() {
        Optional<Person> person = personJpaRepository.findById(1L);
        assertTrue(person.isPresent());
        assertEquals("Hans", person.get().getName());
    }

    @Test
    void findById_WhenPersonNotExists_ShouldReturnEmptyOptional() {
        Optional<Person> person = personJpaRepository.findById(999L);
        assertFalse(person.isPresent());
    }

    @Test
    void save_ShouldPersistPerson() {
        Person newPerson = new Person();
        newPerson.setName("Test");
        newPerson.setLastname("User");
        newPerson.setZipcode("12345");
        newPerson.setCity("TestCity");
        
        // Find an existing color to associate with the person
        Color existingColor = colorJpaRepository.findByNameIgnoreCase("rot")
                .orElseGet(() -> colorJpaRepository.save(new Color("rot")));
        newPerson.setColor(existingColor);
        
        Person savedPerson = personJpaRepository.save(newPerson);

        assertNotNull(savedPerson.getId());
        assertEquals("Test", savedPerson.getName());
        assertEquals("rot", savedPerson.getColor().getName());

        Optional<Person> foundPerson = personJpaRepository.findById(savedPerson.getId());
        assertTrue(foundPerson.isPresent());
        assertEquals("Test", foundPerson.get().getName());
        assertEquals("rot", foundPerson.get().getColor().getName());
    }

    @Test
    void deleteById_ShouldRemovePerson() {
        long initialCount = personJpaRepository.count();
        personJpaRepository.deleteById(1L);
        assertEquals(initialCount - 1, personJpaRepository.count());
        assertFalse(personJpaRepository.findById(1L).isPresent());
    }
}
