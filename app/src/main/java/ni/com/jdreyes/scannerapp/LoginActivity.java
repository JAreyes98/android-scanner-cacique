package ni.com.jdreyes.scannerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import ni.com.jdreyes.scannerapp.models.Secret;
import ni.com.jdreyes.scannerapp.models.UserAuthentication;
import ni.com.jdreyes.scannerapp.rest.conf.RetrofitFactory;
import ni.com.jdreyes.scannerapp.rest.service.AuthenticationService;
import ni.com.jdreyes.scannerapp.utils.ChekingPermission;
import ni.com.jdreyes.scannerapp.utils.UserStaticInfo;
import ni.com.jdreyes.scannerapp.utils.enums.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final AuthenticationService authenticationService
            = RetrofitFactory.createService(AuthenticationService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnLogin).setOnClickListener(onClickLogin());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ChekingPermission.checkPermissions(this);
            return;
        }
    }

    public View.OnClickListener onClickLogin() {
        return v -> {

            UserAuthentication authentication =new UserAuthentication();
            authentication.setUsername(getTextForElement(R.id.editUsername));
            authentication.setPassword(getTextForElement(R.id.editPassword));

//            if (Objects.isNull(authentication.getUsername()) || authentication.getUsername().isEmpty()) {
//                Toast.makeText(this, "Se debe especificar un usuario", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (Objects.isNull(authentication.getPassword()) || authentication.getPassword().isEmpty()) {
//                Toast.makeText(this, "Se debe especificar una contraseña", Toast.LENGTH_SHORT).show();
//                return;
//            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ChekingPermission.checkPermissions(this);
                return;
            }

//            authenticationService.signin(authentication).enqueue(new Callback<Secret>() {
            authenticationService.signin().enqueue(new Callback<Secret>() {
                @Override
                public void onResponse(Call<Secret> call, Response<Secret> response) {
                    HttpStatus httpStatus = HttpStatus.resolve(response.code());
                    if (response.isSuccessful() &&  httpStatus == HttpStatus.OK) {
                        UserStaticInfo.setBaseAuth(response.body().getToken());
                        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Secret> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        };
    }

    private String getTextForElement(@IdRes int id) {
        return ((EditText)findViewById(id)).getText().toString();
    }
}