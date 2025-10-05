package com.assecor.assessment.service;

import com.assecor.assessment.model.Color;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Color operations.
 * Provides business logic for color management.
 */
public interface ColorService {
    
    /**
     * Retrieve all colors.
     * 
     * @return list of all colors
     */
    List<Color> getAllColors();
    
    /**
     * Retrieve a color by its ID.
     * 
     * @param id the color ID
     * @return Optional containing the color if found
     */
    Optional<Color> getColorById(Long id);
    
    /**
     * Create a new color.
     * 
     * @param color the color to create
     * @return the created color
     * @throws IllegalArgumentException if color with same name already exists
     */
    Color createColor(Color color);
    
    /**
     * Update an existing color.
     * 
     * @param id the color ID to update
     * @param color the updated color data
     * @return the updated color
     * @throws IllegalArgumentException if color not found or name conflict
     */
    Color updateColor(Long id, Color color);
    
    /**
     * Delete a color by its ID.
     * 
     * @param id the color ID to delete
     * @throws IllegalArgumentException if color not found
     */
    void deleteColor(Long id);
}
