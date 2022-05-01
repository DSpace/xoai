package io.gdcc.xoai.dataprovider.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryItemTest {

    @Test
    void getSets() {
        // given
        InMemoryItem subject = InMemoryItem.randomItem();
        subject.withSet("test");
        
        // when
        List<Set> result = subject.getSets();
        
        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(new Set("test")));
    }
    
}