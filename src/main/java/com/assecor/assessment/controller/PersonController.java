package com.assecor.assessment.controller;

import com.assecor.assessment.dto.PersonInputDto;
import com.assecor.assessment.dto.PersonMapper;
import com.assecor.assessment.model.Person;
import com.assecor.assessment.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
@Tag(name = "Person Management", description = "APIs for managing persons and their favorite colors")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;
    private final PersonMapper personMapper;
    
    public PersonController(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    @GetMapping
    @Operation(summary = "Get all persons", description = "Retrieve a list of all persons")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        logger.info("Retrieving all persons");
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get person by ID", description = "Retrieve a specific person by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<Person> getPersonById(
            @Parameter(description = "ID of the person to retrieve") 
            @PathVariable int id) {
        logger.info("Retrieving person with ID: {}", id);
        Optional<Person> person = personService.getPersonById(id);
        return person.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/color/{color}")
    @Operation(summary = "Get persons by color", description = "Retrieve all persons with a specific favorite color")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved persons with the specified color")
    public ResponseEntity<List<Person>> getPersonsByColor(
            @Parameter(description = "Color to filter by") 
            @PathVariable String color) {
        logger.info("Retrieving persons with color: {}", color);
        List<Person> persons = personService.getPersonsByColor(color);
        return ResponseEntity.ok(persons);
    }

    @PostMapping
    @Operation(summary = "Create a new person", description = "Add a new person to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Person> createPerson(@Valid @RequestBody PersonInputDto personInputDto) {
        logger.info("Creating new person: {}", personInputDto);
        Person person = personMapper.toEntity(personInputDto);
        Person savedPerson = personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a person", description = "Update an existing person by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Person> updatePerson(
            @Parameter(description = "ID of the person to update") 
            @PathVariable int id,
            @Valid @RequestBody PersonInputDto personInputDto) {
        logger.info("Updating person with ID: {}", id);
        Person person = personMapper.toEntity(personInputDto);
        Optional<Person> updatedPerson = personService.updatePerson(id, person);
        if (updatedPerson.isPresent()) {
            return ResponseEntity.ok(updatedPerson.get());
        } else {
            logger.warn("Person with ID {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a person", description = "Delete a person by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<Void> deletePerson(
            @Parameter(description = "ID of the person to delete") 
            @PathVariable int id) {
        logger.info("Deleting person with ID: {}", id);
        boolean deleted = personService.deletePerson(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Person with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}
