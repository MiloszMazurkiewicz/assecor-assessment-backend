package com.assecor.assessment.controller;

import com.assecor.assessment.model.Color;
import com.assecor.assessment.service.ColorService;
import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/colors")
public class ColorController {
    
    private static final Logger logger = LoggerFactory.getLogger(ColorController.class);
    
    @Autowired
    private ColorService colorService;
    
    @Autowired
    private Counter colorRetrievalCounter;
    
    @Autowired
    private Counter colorCreationCounter;
    
    @Autowired
    private Counter colorUpdateCounter;
    
    @Autowired
    private Counter colorDeletionCounter;
    
    @GetMapping
    public ResponseEntity<List<Color>> getAllColors() {
        logger.debug("Retrieving all colors");
        colorRetrievalCounter.increment();
        List<Color> colors = colorService.getAllColors();
        logger.debug("Found {} colors", colors.size());
        return ResponseEntity.ok(colors);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable Long id) {
        logger.debug("Retrieving color with ID: {}", id);
        colorRetrievalCounter.increment();
        Optional<Color> color = colorService.getColorById(id);
        if (color.isPresent()) {
            logger.debug("Color found: {}", color.get());
            return ResponseEntity.ok(color.get());
        } else {
            logger.debug("Color with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Color> createColor(@Valid @RequestBody Color color) {
        logger.info("Creating new color: {}", color);
        try {
            Color savedColor = colorService.createColor(color);
            colorCreationCounter.increment();
            logger.info("Color created successfully with ID: {}", savedColor.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedColor);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to create color: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Color> updateColor(@PathVariable Long id, @Valid @RequestBody Color color) {
        logger.info("Updating color with ID: {} with data: {}", id, color);
        try {
            Color updatedColor = colorService.updateColor(id, color);
            colorUpdateCounter.increment();
            logger.info("Color with ID {} updated successfully", id);
            return ResponseEntity.ok(updatedColor);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to update color: {}", e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Long id) {
        logger.info("Deleting color with ID: {}", id);
        try {
            colorService.deleteColor(id);
            colorDeletionCounter.increment();
            logger.info("Color with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to delete color: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
