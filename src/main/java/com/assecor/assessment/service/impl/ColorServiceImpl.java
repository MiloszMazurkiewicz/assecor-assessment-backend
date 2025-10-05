package com.assecor.assessment.service.impl;

import com.assecor.assessment.model.Color;
import com.assecor.assessment.repository.ColorJpaRepository;
import com.assecor.assessment.service.ColorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ColorService interface.
 * Provides business logic for color management operations.
 */
@Service
public class ColorServiceImpl implements ColorService {

    private static final Logger logger = LoggerFactory.getLogger(ColorServiceImpl.class);
    
    private final ColorJpaRepository colorJpaRepository;

    public ColorServiceImpl(ColorJpaRepository colorJpaRepository) {
        this.colorJpaRepository = colorJpaRepository;
    }

    @Override
    public List<Color> getAllColors() {
        logger.debug("Retrieving all colors from service layer");
        List<Color> colors = colorJpaRepository.findAll();
        logger.debug("Found {} colors", colors.size());
        return colors;
    }

    @Override
    public Optional<Color> getColorById(Long id) {
        logger.debug("Retrieving color with ID: {} from service layer", id);
        Optional<Color> color = colorJpaRepository.findById(id);
        if (color.isPresent()) {
            logger.debug("Color found: {}", color.get());
        } else {
            logger.debug("Color with ID {} not found", id);
        }
        return color;
    }

    @Override
    public Color createColor(Color color) {
        logger.info("Creating new color: {}", color);
        
        // Check if color with same name already exists
        Optional<Color> existingColor = colorJpaRepository.findByNameIgnoreCase(color.getName());
        if (existingColor.isPresent()) {
            logger.warn("Color with name '{}' already exists", color.getName());
            throw new IllegalArgumentException("Color with name '" + color.getName() + "' already exists");
        }
        
        Color savedColor = colorJpaRepository.save(color);
        logger.info("Color created successfully with ID: {}", savedColor.getId());
        return savedColor;
    }

    @Override
    public Color updateColor(Long id, Color color) {
        logger.info("Updating color with ID: {} with data: {}", id, color);
        
        // Check if color exists
        Optional<Color> existingColor = colorJpaRepository.findById(id);
        if (existingColor.isEmpty()) {
            logger.warn("Color with ID {} not found for update", id);
            throw new IllegalArgumentException("Color with ID " + id + " not found");
        }
        
        // Check if another color with same name exists (excluding current one)
        Optional<Color> colorWithSameName = colorJpaRepository.findByNameIgnoreCase(color.getName());
        if (colorWithSameName.isPresent() && !colorWithSameName.get().getId().equals(id)) {
            logger.warn("Another color with name '{}' already exists", color.getName());
            throw new IllegalArgumentException("Another color with name '" + color.getName() + "' already exists");
        }
        
        // Update the color data
        color.setId(id);
        Color updatedColor = colorJpaRepository.save(color);
        logger.info("Color with ID {} updated successfully", id);
        return updatedColor;
    }

    @Override
    public void deleteColor(Long id) {
        logger.info("Deleting color with ID: {}", id);
        
        // Check if color exists
        Optional<Color> existingColor = colorJpaRepository.findById(id);
        if (existingColor.isEmpty()) {
            logger.warn("Color with ID {} not found for deletion", id);
            throw new IllegalArgumentException("Color with ID " + id + " not found");
        }
        
        // Check if color is being used by any persons
        // Note: This would require a method in PersonJpaRepository to check for color usage
        // For now, we'll allow deletion and let the database handle foreign key constraints
        
        colorJpaRepository.deleteById(id);
        logger.info("Color with ID {} deleted successfully", id);
    }
}
