package ni.com.jdreyes.scannerapp.fragments.adapters;

import android.content.Context;

import java.util.List;

import ni.com.jdreyes.scannerapp.models.InventarioActivo;

public class InventarioActivoAdapter extends AbstractAdapter<InventarioActivo> {

  public InventarioActivoAdapter(
      Context context, int textViewResourceId, List<InventarioActivo> data) {
    super(context, textViewResourceId, data);
  }

  @Override
  public String getPlaceHolder() {
    return "SELECCIONE EL INVENTARIO";
  }

  @Override
  public String onSelectShowText(InventarioActivo obj) {
    return String.format("%s | %s %s -> %s",
            obj.getId_Inventario().toString(),
            obj.getNombre_Bod_Origen(),
            obj.getFecha(),
            obj.getBodega_Destino() == null ? "-" : obj.getBodega_Destino());
  }
}
