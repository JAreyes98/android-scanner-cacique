package ni.com.jdreyes.scannerapp.models;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenCarga {
    private String id;
    private String status;
    private Timestamp orderDate;
    private String route;
    private String sellerCode;
    private String customer;
}
