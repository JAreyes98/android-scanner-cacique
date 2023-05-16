package ni.com.jdreyes.scannerapp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventarioBodega {
    private String date;
    private Bodega bodega;
    private String barcode;
    private String idInventario;
}
