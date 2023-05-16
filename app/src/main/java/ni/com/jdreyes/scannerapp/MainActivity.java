package ni.com.jdreyes.scannerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ni.com.jdreyes.scannerapp.fragments.bodega.BodegaFragment;
import ni.com.jdreyes.scannerapp.fragments.cargacamion.CargaInvFragment;
import ni.com.jdreyes.scannerapp.fragments.despacho.ProduccionInvFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment despacho, carga, bodega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        despacho = ProduccionInvFragment.newInstance();
        carga = CargaInvFragment.newInstance();
        bodega = BodegaFragment.newInstance();

        //Activity onBackEvent
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(getApplicationContext(), "No puede ir mas atras", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void onClick(View view) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.btnDespacho:
                transaction.replace(R.id.frameContainer, despacho).commit();
                break;
            case R.id.btnCarga:
                transaction.replace(R.id.frameContainer, carga).commit();
                break;
            case R.id.btnBodega:
                transaction.replace(R.id.frameContainer, bodega).commit();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Opcion no es valida", Toast.LENGTH_LONG).show();
                break;
        }
    }
}