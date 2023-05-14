package ni.com.jdreyes.scannerapp.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Planta  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String planta;
    private String nombre;
    private int produccion;
}
