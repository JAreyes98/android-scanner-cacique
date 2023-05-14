package ni.com.jdreyes.scannerapp.rest.service;

import ni.com.jdreyes.scannerapp.models.Bodega;
import ni.com.jdreyes.scannerapp.models.Camion;
import ni.com.jdreyes.scannerapp.models.OrdenCarga;
import ni.com.jdreyes.scannerapp.models.Planta;
import ni.com.jdreyes.scannerapp.models.wrapper.DataWrapper;
import ni.com.jdreyes.scannerapp.models.wrapper.ListDataWrapper;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CatalogService {
    @GET("v1/catalogs/plants")
    Call<DataWrapper<Planta>> fetchPlantas();
    @GET("v1/catalogs/trucks")
    Call<ListDataWrapper<Camion>> fetchCamiones();
    @GET("v1/catalogs/warehouse")
    Call<DataWrapper<Bodega>> fetchBodegas();
    @GET("v1/catalogs/orders")
    Call<ListDataWrapper<OrdenCarga>> fetchOrdenesCarga();
}
