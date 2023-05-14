package ni.com.jdreyes.scannerapp.fragments.adapters;

import android.content.Context;

import java.util.List;

import ni.com.jdreyes.scannerapp.models.Camion;

public class CamionAdapter extends AbstractAdapter<Camion> {

  public CamionAdapter(
      Context context, int textViewResourceId, List<Camion> data) {
    super(context, textViewResourceId, data);
  }

  @Override
  public String getPlaceHolder() {
    return "SELECCIONE";
  }

  @Override
  public String onSelectShowText(Camion obj) {
    return String.format("%s - %s", obj.getPlaca(), obj.getNombre());
  }
}
