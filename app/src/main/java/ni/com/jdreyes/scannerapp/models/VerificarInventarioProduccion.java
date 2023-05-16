package ni.com.jdreyes.scannerapp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificarInventarioProduccion {
    private String date;
    private Planta planta;
    private String barcode;
    private String idInventario;
}
