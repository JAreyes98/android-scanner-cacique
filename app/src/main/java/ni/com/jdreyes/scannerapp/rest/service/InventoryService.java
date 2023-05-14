package ni.com.jdreyes.scannerapp.rest.service;

import ni.com.jdreyes.scannerapp.models.VerificarOrden;
import ni.com.jdreyes.scannerapp.models.wrapper.DataWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InventoryService {

        @POST("v1/inventory/order")
        Call<DataWrapper<String>> checkOrder(@Body VerificarOrden userAuthentication);
}
