package ni.com.jdreyes.scannerapp.models;

import java.util.List;

public class BulkRequest {
    private String tipoInventario;
    private int inventarioActivo;

    public String getTipoInventario() {
        return tipoInventario;
    }

    public void setTipoInventario(String tipoInventario) {
        this.tipoInventario = tipoInventario;
    }

    public int getInventarioActivo() {
        return inventarioActivo;
    }

    public void setInventarioActivo(int inventarioActivo) {
        this.inventarioActivo = inventarioActivo;
    }

    public List<BulkItem> getItems() {
        return items;
    }

    public void setItems(List<BulkItem> items) {
        this.items = items;
    }

    private List<BulkItem> items;
}
