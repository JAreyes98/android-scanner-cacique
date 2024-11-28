package ni.com.jdreyes.scannerapp.rest.service;

import java.util.List;

import ni.com.jdreyes.scannerapp.models.InventarioActivo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InventarioActivoService {
    @GET("v1/inventario/listaactivos/{tipoInventarioId}")
    Call<List<InventarioActivo>> obtenerInventarioActivo(@Path("tipoInventarioId") String tipoInventarioId);
}
