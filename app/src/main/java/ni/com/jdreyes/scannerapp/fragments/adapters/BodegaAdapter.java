package ni.com.jdreyes.scannerapp.fragments.adapters;

import android.content.Context;

import java.util.List;

import ni.com.jdreyes.scannerapp.models.Bodega;

public class BodegaAdapter extends AbstractAdapter<Bodega> {

  public BodegaAdapter(
      Context context, int textViewResourceId, List<Bodega> data) {
    super(context, textViewResourceId, data);
  }

  @Override
  public String getPlaceHolder() {
    return "SELECCIONE";
  }

  @Override
  public String onSelectShowText(Bodega obj) {
    return String.format("%s - %s", obj.getCodigo(), obj.getNombre());
  }
}
