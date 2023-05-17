package ni.com.jdreyes.scannerapp.fragments.adapters;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.List;

import ni.com.jdreyes.scannerapp.models.Inventario;

public class InventarioAdapter extends AbstractAdapter<Inventario> {

    public InventarioAdapter(
            Context context, int textViewResourceId, List<Inventario> data) {
        super(context, textViewResourceId, data);
    }


    @Override
    public String getPlaceHolder() {
        return "SELECCIONE";
    }

    @Override
    public String onSelectShowText(Inventario obj) {
        return obj.getDescription();
    }
}
