package ni.com.jdreyes.scannerapp.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Camion  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String nombre;
    private String placa;
    private Integer pesoMaximo;
    private Integer pesoMinimo;
}
