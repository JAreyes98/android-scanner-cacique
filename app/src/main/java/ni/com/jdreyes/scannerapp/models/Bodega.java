package ni.com.jdreyes.scannerapp.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bodega implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nombre;
}

