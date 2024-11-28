package ni.com.jdreyes.scannerapp.rest.service;

import java.util.List;

import ni.com.jdreyes.scannerapp.models.TipoInventario;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TipoInventarioService {
    @GET("v1/inventario/tpo")
    Call<List<TipoInventario>> obtenerTipoInventario();
}
