package ni.com.jdreyes.scannerapp.rest.service;

import ni.com.jdreyes.scannerapp.models.Producto;
import ni.com.jdreyes.scannerapp.models.wrapper.DataWrapper;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductService {
//    @GET("v1/products/{barcode}")
//    Call<DataWrapper<Producto>> fetchProduct(@Path("barcode") String barcode);
    @GET("product.json")
    Call<DataWrapper<Producto>> fetchProduct();
}
