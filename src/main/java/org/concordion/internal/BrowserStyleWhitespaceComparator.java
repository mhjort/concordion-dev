package org.concordion.internal;

import java.util.Comparator;

public class BrowserStyleWhitespaceComparator implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
//        System.out.println("Expected: " + normalize(o2));
//        System.out.println("Actual:   " + normalize(o1));
        return normalize(o1).compareTo(normalize(o2));
    }

    public static String normalize(final Object object) {
        String s = convertObjectToString(object);
        s = processLineContinuations(s);
        s = replaceMultipleWhitespaceWithOneSpace(s);
        return s.trim();
    }

    private static String convertObjectToString(Object object) {
        if (object == null) {
            return "(null)";
        }
        return "" + object;
    }

    private static String replaceMultipleWhitespaceWithOneSpace(String s) {
        return s.replaceAll("[\\s\\n\\r]+", " ");
    }

    private static String processLineContinuations(String s) {
        return s.replaceAll(" _[\n\r+]", "");
    }
        
}
