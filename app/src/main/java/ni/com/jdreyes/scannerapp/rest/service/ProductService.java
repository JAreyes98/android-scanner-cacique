package ni.com.jdreyes.scannerapp.rest.service;

import ni.com.jdreyes.scannerapp.models.Producto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {
    @GET("v1/products/{barcode}")
    Call<Producto> fetchProduct(@Path("barcode") String barcode);
}
