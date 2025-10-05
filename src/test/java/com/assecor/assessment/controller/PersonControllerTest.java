package com.assecor.assessment.controller;

import com.assecor.assessment.model.Person;
import com.assecor.assessment.model.Color;
import com.assecor.assessment.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person testPerson1;
    private Person testPerson2;

    @BeforeEach
    void setUp() {
        testPerson1 = new Person();
        testPerson1.setId(1L);
        testPerson1.setName("Hans");
        testPerson1.setLastname("Müller");
        testPerson1.setZipcode("67742");
        testPerson1.setCity("Lauterecken");
        Color color1 = new Color();
        color1.setId(1L);
        color1.setName("blau");
        testPerson1.setColor(color1);
        
        testPerson2 = new Person();
        testPerson2.setId(2L);
        testPerson2.setName("Peter");
        testPerson2.setLastname("Petersen");
        testPerson2.setZipcode("18439");
        testPerson2.setCity("Stralsund");
        Color color2 = new Color();
        color2.setId(2L);
        color2.setName("grün");
        testPerson2.setColor(color2);
    }

    @Test
    void getAllPersons_ShouldReturnAllPersons() throws Exception {
        // Mock the service to return test data
        when(personService.getAllPersons()).thenReturn(List.of(testPerson1, testPerson2));
        
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hans"))
                .andExpect(jsonPath("$[0].lastname").value("Müller"))
                .andExpect(jsonPath("$[0].zipcode").value("67742"))
                .andExpect(jsonPath("$[0].city").value("Lauterecken"))
                .andExpect(jsonPath("$[0].color.name").value("blau"));
    }

    @Test
    void getPersonById_WhenPersonExists_ShouldReturnPerson() throws Exception {
        // Mock the service to return test data
        when(personService.getPersonById(1)).thenReturn(Optional.of(testPerson1));
        
        mockMvc.perform(get("/persons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hans"))
                .andExpect(jsonPath("$.lastname").value("Müller"))
                .andExpect(jsonPath("$.zipcode").value("67742"))
                .andExpect(jsonPath("$.city").value("Lauterecken"))
                .andExpect(jsonPath("$.color.name").value("blau"));
    }

    @Test
    void getPersonById_WhenPersonNotExists_ShouldReturn404() throws Exception {
        // Mock the service to return empty optional
        when(personService.getPersonById(999)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/persons/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPersonsByColor_ShouldReturnPersonsWithMatchingColor() throws Exception {
        // Mock the service to return test data
        when(personService.getPersonsByColor("blau")).thenReturn(List.of(testPerson1));
        
        mockMvc.perform(get("/persons/color/blau"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].color.name").value("blau"));
    }

    @Test
    void createPerson_ShouldReturnCreatedPerson() throws Exception {
        Person newPerson = new Person();
        newPerson.setName("John");
        newPerson.setLastname("Doe");
        newPerson.setZipcode("12345");
        newPerson.setCity("TestCity");
        Color color = new Color();
        color.setName("rot");
        newPerson.setColor(color);
        
        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setName("John");
        savedPerson.setLastname("Doe");
        savedPerson.setZipcode("12345");
        savedPerson.setCity("TestCity");
        savedPerson.setColor(color);

        // Mock the service to return saved person
        when(personService.createPerson(any(Person.class))).thenReturn(savedPerson);

        mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPerson)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.zipcode").value("12345"))
                .andExpect(jsonPath("$.city").value("TestCity"))
                .andExpect(jsonPath("$.color.name").value("rot"));
    }
}
