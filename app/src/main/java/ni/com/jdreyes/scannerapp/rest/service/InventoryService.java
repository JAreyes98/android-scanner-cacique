package ni.com.jdreyes.scannerapp.rest.service;

import ni.com.jdreyes.scannerapp.models.InventarioBodega;
import ni.com.jdreyes.scannerapp.models.VerificarInventarioProduccion;
import ni.com.jdreyes.scannerapp.models.VerificarOrden;
import ni.com.jdreyes.scannerapp.models.wrapper.DataWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InventoryService {

        @POST("v1/inventory/order")
        Call<DataWrapper<String>> checkOrder(@Body VerificarOrden object);

        @POST("v1/inventory/production")
        Call<DataWrapper<String>> checkProductionInventory(@Body VerificarInventarioProduccion object);

        @POST("v1/inventory/warehouse")
        Call<DataWrapper<String>> checkWarehouseInventory(@Body InventarioBodega object);
}
