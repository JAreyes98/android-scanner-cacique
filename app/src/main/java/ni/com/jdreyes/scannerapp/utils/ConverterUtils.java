package ni.com.jdreyes.scannerapp.utils;

import java.math.BigDecimal;

public interface ConverterUtils {
    default Integer getEntero(String numero) {
        try {
            return Integer.parseInt(numero);
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("No se puede convertir %s a enterio", numero));
        }
    }

    default BigDecimal getBigDecimal(String numero) {
        try {
            return new BigDecimal(numero);
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("No se puede convertir %s a enterio", numero));
        }
    }
}
