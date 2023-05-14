package ni.com.jdreyes.scannerapp.utils;


import java.util.function.Predicate;

public interface ThrowableUtils {

    default <T> void  throwIf(Predicate<T> criteria, T obj, String message) {
        if (criteria.test(obj))
            throw new RuntimeException(message);
    }

}
