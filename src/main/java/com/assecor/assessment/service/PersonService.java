package com.assecor.assessment.service;

import com.assecor.assessment.model.Person;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Person entities.
 * This layer contains business logic and acts as an abstraction
 * between the controller and repository layers.
 */
public interface PersonService {
    
    /**
     * Retrieves all persons.
     * 
     * @return List of all persons
     */
    List<Person> getAllPersons();
    
    /**
     * Retrieves a person by their ID.
     * 
     * @param id the person ID
     * @return Optional containing the person if found, empty otherwise
     */
    Optional<Person> getPersonById(int id);
    
    /**
     * Retrieves all persons with a specific color.
     * 
     * @param color the color to search for
     * @return List of persons with the specified color
     */
    List<Person> getPersonsByColor(String color);
    
    /**
     * Creates a new person.
     * 
     * @param person the person to create
     * @return the created person
     */
    Person createPerson(Person person);
    
    /**
     * Updates an existing person.
     * 
     * @param id the person ID
     * @param person the updated person data
     * @return the updated person
     */
    Person updatePerson(int id, Person person);
    
    /**
     * Deletes a person by ID.
     * 
     * @param id the person ID to delete
     */
    void deletePerson(int id);
}
