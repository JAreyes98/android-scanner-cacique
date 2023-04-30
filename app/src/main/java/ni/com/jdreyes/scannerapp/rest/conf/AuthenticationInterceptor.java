package ni.com.jdreyes.scannerapp.rest.conf;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String token;

    public AuthenticationInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

//        String credentials = username + ":" + password;
//        // create Base64 encodet string
//        final String basic =
//                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Request.Builder builder = original.newBuilder()
                .header("Authorization", token);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
