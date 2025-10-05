package com.assecor.assessment.service.impl;

import com.assecor.assessment.model.Color;
import com.assecor.assessment.repository.ColorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ColorServiceImpl.
 * Tests business logic, validation, and error handling.
 */
@ExtendWith(MockitoExtension.class)
class ColorServiceImplTest {

    @Mock
    private ColorJpaRepository colorJpaRepository;

    @InjectMocks
    private ColorServiceImpl colorService;

    private Color testColor;
    private Color anotherColor;

    @BeforeEach
    void setUp() {
        testColor = new Color(1L, "blau");
        anotherColor = new Color(2L, "grün");
    }

    @Test
    void getAllColors_ShouldReturnAllColors() {
        // Given
        List<Color> expectedColors = Arrays.asList(testColor, anotherColor);
        when(colorJpaRepository.findAll()).thenReturn(expectedColors);

        // When
        List<Color> result = colorService.getAllColors();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testColor));
        assertTrue(result.contains(anotherColor));
        verify(colorJpaRepository).findAll();
    }

    @Test
    void getColorById_WhenColorExists_ShouldReturnColor() {
        // Given
        Long colorId = 1L;
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.of(testColor));

        // When
        Optional<Color> result = colorService.getColorById(colorId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testColor, result.get());
        verify(colorJpaRepository).findById(colorId);
    }

    @Test
    void getColorById_WhenColorNotExists_ShouldReturnEmpty() {
        // Given
        Long colorId = 999L;
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.empty());

        // When
        Optional<Color> result = colorService.getColorById(colorId);

        // Then
        assertFalse(result.isPresent());
        verify(colorJpaRepository).findById(colorId);
    }

    @Test
    void createColor_WithValidColor_ShouldReturnSavedColor() {
        // Given
        Color newColor = new Color("rot");
        Color savedColor = new Color(3L, "rot");
        when(colorJpaRepository.findByNameIgnoreCase("rot")).thenReturn(Optional.empty());
        when(colorJpaRepository.save(newColor)).thenReturn(savedColor);

        // When
        Color result = colorService.createColor(newColor);

        // Then
        assertNotNull(result);
        assertEquals(savedColor, result);
        verify(colorJpaRepository).findByNameIgnoreCase("rot");
        verify(colorJpaRepository).save(newColor);
    }

    @Test
    void createColor_WithExistingColorName_ShouldThrowException() {
        // Given
        Color newColor = new Color("blau");
        when(colorJpaRepository.findByNameIgnoreCase("blau")).thenReturn(Optional.of(testColor));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> colorService.createColor(newColor));
        
        assertEquals("Color with name 'blau' already exists", exception.getMessage());
        verify(colorJpaRepository).findByNameIgnoreCase("blau");
        verify(colorJpaRepository, never()).save(any(Color.class));
    }

    @Test
    void createColor_WithCaseInsensitiveName_ShouldThrowException() {
        // Given
        Color newColor = new Color("BLAU"); // Different case
        when(colorJpaRepository.findByNameIgnoreCase("BLAU")).thenReturn(Optional.of(testColor));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> colorService.createColor(newColor));
        
        assertEquals("Color with name 'BLAU' already exists", exception.getMessage());
        verify(colorJpaRepository).findByNameIgnoreCase("BLAU");
        verify(colorJpaRepository, never()).save(any(Color.class));
    }

    @Test
    void updateColor_WhenColorExists_ShouldReturnUpdatedColor() {
        // Given
        Long colorId = 1L;
        Color updatedColorData = new Color("updated");
        Color savedColor = new Color(1L, "updated");
        
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.of(testColor));
        when(colorJpaRepository.findByNameIgnoreCase("updated")).thenReturn(Optional.empty());
        when(colorJpaRepository.save(any(Color.class))).thenReturn(savedColor);

        // When
        Color result = colorService.updateColor(colorId, updatedColorData);

        // Then
        assertNotNull(result);
        assertEquals(savedColor, result);
        verify(colorJpaRepository).findById(colorId);
        verify(colorJpaRepository).findByNameIgnoreCase("updated");
        verify(colorJpaRepository).save(any(Color.class));
    }

    @Test
    void updateColor_WhenColorNotExists_ShouldThrowException() {
        // Given
        Long colorId = 999L;
        Color updatedColorData = new Color("updated");
        
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> colorService.updateColor(colorId, updatedColorData));
        
        assertEquals("Color with ID 999 not found", exception.getMessage());
        verify(colorJpaRepository).findById(colorId);
        verify(colorJpaRepository, never()).save(any(Color.class));
    }

    @Test
    void updateColor_WithExistingNameForDifferentColor_ShouldThrowException() {
        // Given
        Long colorId = 1L;
        Color updatedColorData = new Color("grün"); // Same name as anotherColor
        
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.of(testColor));
        when(colorJpaRepository.findByNameIgnoreCase("grün")).thenReturn(Optional.of(anotherColor));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> colorService.updateColor(colorId, updatedColorData));
        
        assertEquals("Another color with name 'grün' already exists", exception.getMessage());
        verify(colorJpaRepository).findById(colorId);
        verify(colorJpaRepository).findByNameIgnoreCase("grün");
        verify(colorJpaRepository, never()).save(any(Color.class));
    }

    @Test
    void updateColor_WithSameNameForSameColor_ShouldUpdateSuccessfully() {
        // Given
        Long colorId = 1L;
        Color updatedColorData = new Color("blau"); // Same name as testColor
        
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.of(testColor));
        when(colorJpaRepository.findByNameIgnoreCase("blau")).thenReturn(Optional.of(testColor));
        when(colorJpaRepository.save(any(Color.class))).thenReturn(testColor);

        // When
        Color result = colorService.updateColor(colorId, updatedColorData);

        // Then
        assertNotNull(result);
        assertEquals(testColor, result);
        verify(colorJpaRepository).findById(colorId);
        verify(colorJpaRepository).findByNameIgnoreCase("blau");
        verify(colorJpaRepository).save(any(Color.class));
    }

    @Test
    void deleteColor_WhenColorExists_ShouldDeleteColor() {
        // Given
        Long colorId = 1L;
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.of(testColor));

        // When
        colorService.deleteColor(colorId);

        // Then
        verify(colorJpaRepository).findById(colorId);
        verify(colorJpaRepository).deleteById(colorId);
    }

    @Test
    void deleteColor_WhenColorNotExists_ShouldThrowException() {
        // Given
        Long colorId = 999L;
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> colorService.deleteColor(colorId));
        
        assertEquals("Color with ID 999 not found", exception.getMessage());
        verify(colorJpaRepository).findById(colorId);
        verify(colorJpaRepository, never()).deleteById(anyLong());
    }

    @Test
    void createColor_WithNullName_ShouldHandleGracefully() {
        // Given
        Color newColor = new Color(null);
        when(colorJpaRepository.findByNameIgnoreCase(null)).thenReturn(Optional.empty());
        when(colorJpaRepository.save(newColor)).thenReturn(new Color(3L, null));

        // When
        Color result = colorService.createColor(newColor);

        // Then
        assertNotNull(result);
        verify(colorJpaRepository).findByNameIgnoreCase(null);
        verify(colorJpaRepository).save(newColor);
    }

    @Test
    void createColor_WithEmptyName_ShouldHandleGracefully() {
        // Given
        Color newColor = new Color("");
        when(colorJpaRepository.findByNameIgnoreCase("")).thenReturn(Optional.empty());
        when(colorJpaRepository.save(newColor)).thenReturn(new Color(3L, ""));

        // When
        Color result = colorService.createColor(newColor);

        // Then
        assertNotNull(result);
        verify(colorJpaRepository).findByNameIgnoreCase("");
        verify(colorJpaRepository).save(newColor);
    }

    @Test
    void updateColor_WithCaseInsensitiveNameCheck_ShouldWorkCorrectly() {
        // Given
        Long colorId = 1L;
        Color updatedColorData = new Color("BLAU"); // Different case
        
        when(colorJpaRepository.findById(colorId)).thenReturn(Optional.of(testColor));
        when(colorJpaRepository.findByNameIgnoreCase("BLAU")).thenReturn(Optional.of(testColor));
        when(colorJpaRepository.save(any(Color.class))).thenReturn(testColor);

        // When
        Color result = colorService.updateColor(colorId, updatedColorData);

        // Then
        assertNotNull(result);
        verify(colorJpaRepository).findByNameIgnoreCase("BLAU");
        verify(colorJpaRepository).save(any(Color.class));
    }
}
