package ni.com.jdreyes.scannerapp.rest.conf;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;

import ni.com.jdreyes.scannerapp.utils.UserStaticInfo;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(APIConfiguration.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(
                    new GsonBuilder().serializeNulls().create()
            ));

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    public static <S> S createService(
            Class<S> serviceClass) {
        String token = UserStaticInfo.getAuth();
        if (!TextUtils.isEmpty(token) ) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(token);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }
}
