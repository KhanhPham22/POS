package util;

public class NullCheck {
    @FunctionalInterface
    public interface ValueExtractor<T, R> {
        R extract(T item) throws Exception;
    }
    
    public static <T, R> String NullCheck(T item, ValueExtractor<T, R> extractor, String defaultValue) {
        try {
            if (item == null) return defaultValue;
            R result = extractor.extract(item);
            return result == null ? defaultValue : result.toString();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
