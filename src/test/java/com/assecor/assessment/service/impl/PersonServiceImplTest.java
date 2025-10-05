package com.assecor.assessment.service.impl;

import com.assecor.assessment.model.Color;
import com.assecor.assessment.model.Person;
import com.assecor.assessment.repository.ColorJpaRepository;
import com.assecor.assessment.repository.PersonJpaRepository;import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PersonServiceImpl.
 * Tests business logic, error handling, and metrics collection.
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonJpaRepository personJpaRepository;

    @Mock
    private ColorJpaRepository colorJpaRepository;

    @Mock
    private MeterRegistry meterRegistry;

    private PersonServiceImpl personService;

    private Person testPerson;
    private Color testColor;

    @BeforeEach
    void setUp() {
        // Setup test data
        testColor = new Color(1L, "blau");
        testPerson = new Person(1L, "Hans", "Müller", "67111", "Maxdorf", testColor);

        // Create a real MeterRegistry for testing (SimpleMeterRegistry)
        io.micrometer.core.instrument.simple.SimpleMeterRegistry simpleMeterRegistry = 
            new io.micrometer.core.instrument.simple.SimpleMeterRegistry();
        
        // Create service instance with real MeterRegistry
        personService = new PersonServiceImpl(personJpaRepository, colorJpaRepository, simpleMeterRegistry);
    }

    @Test
    void getAllPersons_ShouldReturnAllPersons() {
        // Given
        List<Person> expectedPersons = Arrays.asList(testPerson);
        when(personJpaRepository.findAll()).thenReturn(expectedPersons);

        // When
        List<Person> result = personService.getAllPersons();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPerson, result.get(0));
        verify(personJpaRepository).findAll();
    }

    @Test
    void getPersonById_WhenPersonExists_ShouldReturnPerson() {
        // Given
        int personId = 1;
        when(personJpaRepository.findById((long) personId)).thenReturn(Optional.of(testPerson));

        // When
        Optional<Person> result = personService.getPersonById(personId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testPerson, result.get());
        verify(personJpaRepository).findById((long) personId);
    }

    @Test
    void getPersonById_WhenPersonNotExists_ShouldReturnEmpty() {
        // Given
        int personId = 999;
        when(personJpaRepository.findById((long) personId)).thenReturn(Optional.empty());

        // When
        Optional<Person> result = personService.getPersonById(personId);

        // Then
        assertFalse(result.isPresent());
        verify(personJpaRepository).findById((long) personId);
    }

    @Test
    void getPersonsByColor_ShouldReturnPersonsWithMatchingColor() {
        // Given
        String colorName = "blau";
        List<Person> expectedPersons = Arrays.asList(testPerson);
        when(personJpaRepository.findByColorNameIgnoreCase(colorName)).thenReturn(expectedPersons);

        // When
        List<Person> result = personService.getPersonsByColor(colorName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPerson, result.get(0));
        verify(personJpaRepository).findByColorNameIgnoreCase(colorName);
    }

    @Test
    void createPerson_WithValidPerson_ShouldReturnSavedPerson() {
        // Given
        Person newPerson = new Person("John", "Doe", "12345", "TestCity", testColor);
        Person savedPerson = new Person(2L, "John", "Doe", "12345", "TestCity", testColor);
        when(personJpaRepository.save(newPerson)).thenReturn(savedPerson);

        // When
        Person result = personService.createPerson(newPerson);

        // Then
        assertNotNull(result);
        assertEquals(savedPerson, result);
        verify(personJpaRepository).save(newPerson);
    }

    @Test
    void createPerson_WithColorNameOnly_ShouldFindExistingColor() {
        // Given
        Color colorWithNameOnly = new Color("blau");
        Person personWithColorName = new Person("John", "Doe", "12345", "TestCity", colorWithNameOnly);
        Person savedPerson = new Person(2L, "John", "Doe", "12345", "TestCity", testColor);
        
        when(colorJpaRepository.findByNameIgnoreCase("blau")).thenReturn(Optional.of(testColor));
        when(personJpaRepository.save(any(Person.class))).thenReturn(savedPerson);

        // When
        Person result = personService.createPerson(personWithColorName);

        // Then
        assertNotNull(result);
        assertEquals(savedPerson, result);
        verify(colorJpaRepository).findByNameIgnoreCase("blau");
        verify(personJpaRepository).save(any(Person.class));
    }

    @Test
    void createPerson_WithNonExistentColor_ShouldThrowException() {
        // Given
        Color colorWithNameOnly = new Color("nonexistent");
        Person personWithColorName = new Person("John", "Doe", "12345", "TestCity", colorWithNameOnly);
        
        when(colorJpaRepository.findByNameIgnoreCase("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.createPerson(personWithColorName));
        
        assertEquals("Color 'nonexistent' not found", exception.getMessage());
        verify(colorJpaRepository).findByNameIgnoreCase("nonexistent");
        verify(personJpaRepository, never()).save(any(Person.class));
    }

    @Test
    void updatePerson_WhenPersonExists_ShouldReturnUpdatedPerson() {
        // Given
        int personId = 1;
        Person updatedPersonData = new Person("Hans", "Updated", "67111", "Maxdorf", testColor);
        Person savedPerson = new Person(1L, "Hans", "Updated", "67111", "Maxdorf", testColor);
        
        when(personJpaRepository.findById((long) personId)).thenReturn(Optional.of(testPerson));
        when(personJpaRepository.save(any(Person.class))).thenReturn(savedPerson);

        // When
        Person result = personService.updatePerson(personId, updatedPersonData);

        // Then
        assertNotNull(result);
        assertEquals(savedPerson, result);
        verify(personJpaRepository).findById((long) personId);
        verify(personJpaRepository).save(any(Person.class));
    }

    @Test
    void updatePerson_WhenPersonNotExists_ShouldThrowException() {
        // Given
        int personId = 999;
        Person updatedPersonData = new Person("Hans", "Updated", "67111", "Maxdorf", testColor);
        
        when(personJpaRepository.findById((long) personId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.updatePerson(personId, updatedPersonData));
        
        assertEquals("Person with ID 999 not found", exception.getMessage());
        verify(personJpaRepository).findById((long) personId);
        verify(personJpaRepository, never()).save(any(Person.class));
    }

    @Test
    void updatePerson_WithColorNameOnly_ShouldFindExistingColor() {
        // Given
        int personId = 1;
        Color colorWithNameOnly = new Color("blau");
        Person personWithColorName = new Person("John", "Doe", "12345", "TestCity", colorWithNameOnly);
        Person savedPerson = new Person(1L, "John", "Doe", "12345", "TestCity", testColor);
        
        when(personJpaRepository.findById((long) personId)).thenReturn(Optional.of(testPerson));
        when(colorJpaRepository.findByNameIgnoreCase("blau")).thenReturn(Optional.of(testColor));
        when(personJpaRepository.save(any(Person.class))).thenReturn(savedPerson);

        // When
        Person result = personService.updatePerson(personId, personWithColorName);

        // Then
        assertNotNull(result);
        assertEquals(savedPerson, result);
        verify(colorJpaRepository).findByNameIgnoreCase("blau");
        verify(personJpaRepository).save(any(Person.class));
    }

    @Test
    void deletePerson_WhenPersonExists_ShouldDeletePerson() {
        // Given
        int personId = 1;
        when(personJpaRepository.findById((long) personId)).thenReturn(Optional.of(testPerson));

        // When
        personService.deletePerson(personId);

        // Then
        verify(personJpaRepository).findById((long) personId);
        verify(personJpaRepository).deleteById((long) personId);
    }

    @Test
    void deletePerson_WhenPersonNotExists_ShouldThrowException() {
        // Given
        int personId = 999;
        when(personJpaRepository.findById((long) personId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> personService.deletePerson(personId));
        
        assertEquals("Person with ID 999 not found", exception.getMessage());
        verify(personJpaRepository).findById((long) personId);
        verify(personJpaRepository, never()).deleteById(anyLong());
    }
}