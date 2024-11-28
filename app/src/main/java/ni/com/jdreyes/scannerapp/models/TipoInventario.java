package ni.com.jdreyes.scannerapp.models;

import java.io.Serializable;

public class TipoInventario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id_tipo_inventario;
    private String Tipo_inventario;

    public String getId_tipo_inventario() {
        return id_tipo_inventario;
    }

    public void setId_tipo_inventario(String id_tipo_inventario) {
        this.id_tipo_inventario = id_tipo_inventario;
    }

    public String getTipo_inventario() {
        return Tipo_inventario;
    }

    public void setTipo_inventario(String tipo_inventario) {
        Tipo_inventario = tipo_inventario;
    }
}
