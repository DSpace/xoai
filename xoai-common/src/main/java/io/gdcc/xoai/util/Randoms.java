package io.gdcc.xoai.util;

import java.security.SecureRandom;

public class Randoms {
    
    private static final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * Create a random String from chars "[a-zA-Z]" with the given length
     *
     * @param number Length of the random String to generate
     */
    public static String randomAlphabetic(int number) {
        int leftBoundary = 'A'; // 65
        int rightBoundary = 'z'; // 122
        int middleLeftBoundary = 'Z'; // 90
        int middleRightBoundary = 'a'; // 97
        
        // ints(origin, boundary) means including origin, excluding boundary!
        return secureRandom.ints(leftBoundary, rightBoundary + 1)
            // do not accept chars from the gap between Z...a
            .filter(i -> i <= middleLeftBoundary || i >= middleRightBoundary)
            .limit(number)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
    
    /**
     * Create a random String from chars "[0-9]" with the given length
     *
     * @param number Length of the random String to generate
     */
    public static String randomNumeric(int number) {
        int leftBoundary = '0'; // 48
        int rightBoundary = '9'; // 57
        
        // ints(origin, boundary) means including origin, excluding boundary!
        return secureRandom.ints(leftBoundary, rightBoundary + 1)
            // do not accept chars from the gap between Z...a
            .limit(number)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
    
}
