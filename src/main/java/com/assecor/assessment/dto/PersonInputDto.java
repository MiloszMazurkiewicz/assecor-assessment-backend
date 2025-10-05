package com.assecor.assessment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonInputDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    
    @JsonProperty("lastname")
    @NotBlank(message = "Lastname is required")
    @Size(min = 1, max = 100, message = "Lastname must be between 1 and 100 characters")
    private String lastname;
    
    @JsonProperty("zipcode")
    @NotBlank(message = "Zipcode is required")
    @Size(min = 5, max = 10, message = "Zipcode must be between 5 and 10 characters")
    private String zipcode;
    
    @JsonProperty("city")
    @NotBlank(message = "City is required")
    @Size(min = 1, max = 100, message = "City must be between 1 and 100 characters")
    private String city;
    
    @JsonProperty("color")
    @NotBlank(message = "Color is required")
    private String color;
}
