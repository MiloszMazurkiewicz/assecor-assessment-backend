package com.assecor.assessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "colors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Color {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;
    
    @Column(name = "name", nullable = false, unique = true, length = 50)
    @JsonProperty("name")
    @NotBlank(message = "Color name is required")
    @Size(min = 1, max = 50, message = "Color name must be between 1 and 50 characters")
    private String name;
    
    
    // Constructor for creating colors without ID
    public Color(String name) {
        this.name = name;
    }
}
