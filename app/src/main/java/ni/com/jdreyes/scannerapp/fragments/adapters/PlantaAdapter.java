package ni.com.jdreyes.scannerapp.fragments.adapters;

import android.content.Context;

import java.util.List;

import ni.com.jdreyes.scannerapp.models.Planta;

public class PlantaAdapter extends AbstractAdapter<Planta> {

  public PlantaAdapter(
      Context context, int textViewResourceId, List<Planta> data) {
    super(context, textViewResourceId, data);
  }

  @Override
  public String getPlaceHolder() {
    return "SELECCIONE";
  }

  @Override
  public String onSelectShowText(Planta obj) {
    return String.format("%s - %s", obj.getPlanta(), obj.getNombre());
  }
}
