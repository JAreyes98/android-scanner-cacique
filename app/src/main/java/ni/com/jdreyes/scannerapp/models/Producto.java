package ni.com.jdreyes.scannerapp.models;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class Producto {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodproduct() {
        return codproduct;
    }

    public void setCodproduct(String codproduct) {
        this.codproduct = codproduct;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    private String name;
    private String codproduct;
    private String barcode;
    private Timestamp createAt;
    private Timestamp updateAt;
}
