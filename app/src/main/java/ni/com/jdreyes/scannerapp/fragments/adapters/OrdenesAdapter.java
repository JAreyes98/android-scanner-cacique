package ni.com.jdreyes.scannerapp.fragments.adapters;

import android.content.Context;

import java.util.List;

import ni.com.jdreyes.scannerapp.models.OrdenCarga;

public class OrdenesAdapter extends AbstractAdapter<OrdenCarga> {

  public OrdenesAdapter(
      Context context, int textViewResourceId, List<OrdenCarga> data) {
    super(context, textViewResourceId, data);
  }

  @Override
  public String getPlaceHolder() {
    return "SELECCIONE";
  }

  @Override
  public String onSelectShowText(OrdenCarga obj) {
    return String.format("Orden No. %d", obj.getId());
  }
}
