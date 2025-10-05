package com.assecor.assessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    
    // Custom constructor for creating new persons without ID
    public Person(String name, String lastname, String zipcode, String city, Color color) {
        this.name = name;
        this.lastname = lastname;
        this.zipcode = zipcode;
        this.city = city;
        this.color = color;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    
    @Column(name = "lastname", nullable = false, length = 100)
    @JsonProperty("lastname")
    @NotBlank(message = "Lastname is required")
    @Size(min = 1, max = 100, message = "Lastname must be between 1 and 100 characters")
    private String lastname;
    
    @Column(name = "zipcode", nullable = false, length = 10)
    @JsonProperty("zipcode")
    @NotBlank(message = "Zipcode is required")
    @Size(min = 5, max = 10, message = "Zipcode must be between 5 and 10 characters")
    private String zipcode;
    
    @Column(name = "city", nullable = false, length = 100)
    @JsonProperty("city")
    @NotBlank(message = "City is required")
    @Size(min = 1, max = 100, message = "City must be between 1 and 100 characters")
    private String city;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", nullable = false)
    @JsonProperty("color")
    private Color color;
}
