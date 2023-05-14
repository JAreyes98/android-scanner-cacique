package ni.com.jdreyes.scannerapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ni.com.jdreyes.scannerapp.fragments.bodega.BodegaFragment;
import ni.com.jdreyes.scannerapp.fragments.cargacamion.CargaInvFragment;
import ni.com.jdreyes.scannerapp.fragments.despacho.ProduccionInvFragment;
import ni.com.jdreyes.scannerapp.utils.UserStaticInfo;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private Fragment despacho, carga, bodega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserStaticInfo.setBaseAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IiQyYSQxNCQyUlN5ejlCMU1UMVQ3bllNSWgzSDh1L01yUlUvTkVGbEZmUHg3ME5ULlF5NVdpRkdQRTBkLiIsInBhc3N3b3JkIjoiSnJleWVzIiwiZXhwIjoxNjg0MDczODk0fQ.-lwzI420q3a2zUIsV9iK7RvBEEIoLGYiNIvQ8OD_0xg");
        setContentView(R.layout.activity_main);

        despacho = ProduccionInvFragment.newInstance();
        carga = CargaInvFragment.newInstance();
        bodega = BodegaFragment.newInstance();
    }

    public void onClick(View view) {

        transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.btnDespacho:
                transaction.replace(R.id.frameContainer, despacho).commit();
                break;
            case R.id.btnCarga:
                transaction.replace(R.id.frameContainer, carga).commit();
                break;
            case R.id.btnBodega:
                transaction.replace(R.id.frameContainer, carga).commit();
                break;
            default:
                break;
        }
    }
}