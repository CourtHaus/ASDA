package org.group1.asda.domain.emotional;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmotionPatternImageLoadTest {

    @Test
    public void testAbstractPatternImagesExist() {
        EmotionalGameState gameState = new EmotionalGameState();
        List<EmotionPattern> patterns = gameState.getPatterns();
        
        for (EmotionPattern pattern : patterns) {
            String imagePath = pattern.getImagePath();
            InputStream stream = getClass().getResourceAsStream(imagePath);
            
            assertNotNull(stream, "Image not found: " + imagePath);
            
            try {
                stream.close();
            } catch (Exception e) {
                // Ignore close errors
            }
        }
    }

    @Test
    public void testFacialEmotionImagesExist() {
        FacialEmotionGameState gameState = new FacialEmotionGameState();
        List<EmotionPattern> patterns = gameState.getPatterns();
        
        for (EmotionPattern pattern : patterns) {
            String imagePath = pattern.getImagePath();
            InputStream stream = getClass().getResourceAsStream(imagePath);
            
            assertNotNull(stream, "Image not found: " + imagePath);
            
            try {
                stream.close();
            } catch (Exception e) {
                // Ignore close errors
            }
        }
    }

    @Test
    public void testAbstractPatternsHaveCorrectPaths() {
        EmotionalGameState gameState = new EmotionalGameState();
        List<EmotionPattern> patterns = gameState.getPatterns();
        
        for (EmotionPattern pattern : patterns) {
            String imagePath = pattern.getImagePath();
            assertTrue(imagePath.startsWith("/images/emotional/"));
            assertTrue(imagePath.endsWith(".png"));
            assertFalse(imagePath.contains("faces"), 
                "Abstract patterns should not use facial images");
        }
    }

    @Test
    public void testFacialPatternsHaveCorrectPaths() {
        FacialEmotionGameState gameState = new FacialEmotionGameState();
        List<EmotionPattern> patterns = gameState.getPatterns();
        
        for (EmotionPattern pattern : patterns) {
            String imagePath = pattern.getImagePath();
            assertTrue(imagePath.startsWith("/images/emotional/faces/"));
            assertTrue(imagePath.endsWith(".jpg"));
        }
    }
}
