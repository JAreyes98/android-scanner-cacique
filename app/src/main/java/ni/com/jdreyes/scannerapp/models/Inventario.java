package ni.com.jdreyes.scannerapp.models;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Inventario {
    private String id;
    private String description;
    private Timestamp date;
    private String planta;
    private String bodega;
    private Integer userId;
    private Integer canceled;
}
