package ni.com.jdreyes.scannerapp.models;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Producto {
    private int id;
    private String name;
    private String codproduct;
    private String barcode;
    private Timestamp createAt;
    private Timestamp updateAt;
}
