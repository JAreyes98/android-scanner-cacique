package ni.com.jdreyes.scannerapp.models;

import java.io.Serializable;
import java.math.BigInteger;

public class InventarioActivo implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigInteger Id_Inventario;
    private String Fecha;
    private String Id_Tipo_Inventario;
    private String Descripion_Tipo;
    private String Bodega_Inventario;
    private String Nombre_Bod_Origen;
    private String Bodega_Destino;
    private String Nombre_Bod_Destino;

    public BigInteger getId_Inventario() {
        return Id_Inventario;
    }

    public void setId_Inventario(BigInteger id_Inventario) {
        Id_Inventario = id_Inventario;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getId_Tipo_Inventario() {
        return Id_Tipo_Inventario;
    }

    public void setId_Tipo_Inventario(String id_Tipo_Inventario) {
        Id_Tipo_Inventario = id_Tipo_Inventario;
    }

    public String getDescripion_Tipo() {
        return Descripion_Tipo;
    }

    public void setDescripion_Tipo(String descripion_Tipo) {
        Descripion_Tipo = descripion_Tipo;
    }

    public String getBodega_Inventario() {
        return Bodega_Inventario;
    }

    public void setBodega_Inventario(String bodega_Inventario) {
        Bodega_Inventario = bodega_Inventario;
    }

    public String getNombre_Bod_Origen() {
        return Nombre_Bod_Origen;
    }

    public void setNombre_Bod_Origen(String nombre_Bod_Origen) {
        Nombre_Bod_Origen = nombre_Bod_Origen;
    }

    public String getBodega_Destino() {
        return Bodega_Destino;
    }

    public void setBodega_Destino(String bodega_Destino) {
        Bodega_Destino = bodega_Destino;
    }

    public String getNombre_Bod_Destino() {
        return Nombre_Bod_Destino;
    }

    public void setNombre_Bod_Destino(String nombre_Bod_Destino) {
        Nombre_Bod_Destino = nombre_Bod_Destino;
    }
}
