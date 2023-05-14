package ni.com.jdreyes.scannerapp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificarOrden {
    private String date;
    private Camion camion;
    private Integer orderId;
    private String barcode;
}
