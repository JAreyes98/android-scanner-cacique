package ni.com.jdreyes.scannerapp.fragments.adapters;

import android.content.Context;

import java.util.List;

import ni.com.jdreyes.scannerapp.models.TipoInventario;

public class TipoInventarioAdapter extends AbstractAdapter<TipoInventario> {

  public TipoInventarioAdapter(
      Context context, int textViewResourceId, List<TipoInventario> data) {
    super(context, textViewResourceId, data);
  }

  @Override
  public String getPlaceHolder() {
    return "SELECCIONE TIPO INVENTARIO";
  }

  @Override
  public String onSelectShowText(TipoInventario obj) {
    return obj.getTipo_inventario();
  }
}
