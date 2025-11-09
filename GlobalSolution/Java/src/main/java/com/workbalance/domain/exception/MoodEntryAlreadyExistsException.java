package com.workbalance.domain.exception;

import java.time.LocalDate;

/**
 * Exception thrown when attempting to create a duplicate mood entry for the same date
 */
public class MoodEntryAlreadyExistsException extends BusinessException {
    
    public MoodEntryAlreadyExistsException(LocalDate date) {
        super("MOOD_ENTRY_EXISTS", 
              String.format("Mood entry already exists for date: %s", date));
    }
}
