package com.assecor.assessment.service.impl;

import com.assecor.assessment.model.Person;
import com.assecor.assessment.model.Color;
import com.assecor.assessment.repository.PersonJpaRepository;
import com.assecor.assessment.repository.ColorJpaRepository;
import com.assecor.assessment.service.PersonService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of PersonService.
 * Contains business logic for managing Person entities.
 */
@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    
    private final PersonJpaRepository personJpaRepository;
    private final ColorJpaRepository colorJpaRepository;
    private final Counter personRetrievalCounter;
    private final Counter personCreationCounter;
    private final Counter personUpdateCounter;
    private final Counter personDeletionCounter;
    private final Counter colorSearchCounter;

    public PersonServiceImpl(PersonJpaRepository personJpaRepository, ColorJpaRepository colorJpaRepository, MeterRegistry meterRegistry) {
        this.personJpaRepository = personJpaRepository;
        this.colorJpaRepository = colorJpaRepository;
        this.personRetrievalCounter = Counter.builder("person.retrieval.total")
                .description("Total number of person retrieval operations")
                .register(meterRegistry);
        this.personCreationCounter = Counter.builder("person.creation.total")
                .description("Total number of person creation operations")
                .register(meterRegistry);
        this.personUpdateCounter = Counter.builder("person.update.total")
                .description("Total number of person update operations")
                .register(meterRegistry);
        this.personDeletionCounter = Counter.builder("person.deletion.total")
                .description("Total number of person deletion operations")
                .register(meterRegistry);
        this.colorSearchCounter = Counter.builder("person.color.search.total")
                .description("Total number of color-based person searches")
                .register(meterRegistry);
    }

    @Override
    public List<Person> getAllPersons() {
        logger.debug("Retrieving all persons from service layer");
        personRetrievalCounter.increment();
        List<Person> persons = personJpaRepository.findAll();
        logger.debug("Found {} persons", persons.size());
        return persons;
    }

    @Override
    public Optional<Person> getPersonById(int id) {
        logger.debug("Retrieving person with ID: {} from service layer", id);
        personRetrievalCounter.increment();
        Optional<Person> person = personJpaRepository.findById((long) id);
        if (person.isPresent()) {
            logger.debug("Person found: {}", person.get());
        } else {
            logger.debug("Person with ID {} not found", id);
        }
        return person;
    }

    @Override
    public List<Person> getPersonsByColor(String color) {
        logger.debug("Retrieving persons with color: {} from service layer", color);
        colorSearchCounter.increment();
        List<Person> persons = personJpaRepository.findByColorNameIgnoreCase(color);
        logger.debug("Found {} persons with color {}", persons.size(), color);
        return persons;
    }

    @Override
    public Person createPerson(Person person) {
        logger.debug("Creating new person: {}", person);
        personCreationCounter.increment();
        
        // If person has a color with only name, find the Color entity
        if (person.getColor() != null && person.getColor().getId() == null) {
            Color existingColor = colorJpaRepository.findByNameIgnoreCase(person.getColor().getName())
                    .orElseThrow(() -> new IllegalArgumentException("Color '" + person.getColor().getName() + "' not found"));
            person.setColor(existingColor);
        }
        
        Person savedPerson = personJpaRepository.save(person);
        logger.info("Person created successfully with ID: {}", savedPerson.getId());
        return savedPerson;
    }

    @Override
    public Optional<Person> updatePerson(int id, Person person) {
        logger.debug("Updating person with ID: {} with data: {}", id, person);
        personUpdateCounter.increment();
        
        // Check if person exists
        Optional<Person> existingPerson = personJpaRepository.findById((long) id);
        if (existingPerson.isEmpty()) {
            logger.warn("Person with ID {} not found for update", id);
            return Optional.empty();
        }
        
        // If person has a color with only name, find the Color entity
        if (person.getColor() != null && person.getColor().getId() == null) {
            Color existingColor = colorJpaRepository.findByNameIgnoreCase(person.getColor().getName())
                    .orElseThrow(() -> new IllegalArgumentException("Color '" + person.getColor().getName() + "' not found"));
            person.setColor(existingColor);
        }
        
        // Update the person data
        person.setId((long) id); // Ensure the ID is set correctly
        Person updatedPerson = personJpaRepository.save(person);
        logger.info("Person with ID {} updated successfully", id);
        return Optional.of(updatedPerson);
    }

    @Override
    public boolean deletePerson(int id) {
        logger.debug("Deleting person with ID: {}", id);
        personDeletionCounter.increment();
        
        // Check if person exists
        Optional<Person> existingPerson = personJpaRepository.findById((long) id);
        if (existingPerson.isEmpty()) {
            logger.warn("Person with ID {} not found for deletion", id);
            return false;
        }
        
        personJpaRepository.deleteById((long) id);
        logger.info("Person with ID {} deleted successfully", id);
        return true;
    }
}
