package ni.com.jdreyes.scannerapp.rest.service;

import ni.com.jdreyes.scannerapp.models.Secret;
import ni.com.jdreyes.scannerapp.models.UserAuthentication;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationService {
    @POST("auth/jwt")
    Call<Secret> signin(@Body UserAuthentication userAuthentication);
//    @GET("login.json")
//    Call<Secret> signin();
}
