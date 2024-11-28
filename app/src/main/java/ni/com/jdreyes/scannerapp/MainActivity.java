package ni.com.jdreyes.scannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import ni.com.jdreyes.scannerapp.fragments.adapters.InventarioActivoAdapter;
import ni.com.jdreyes.scannerapp.fragments.adapters.TipoInventarioAdapter;
import ni.com.jdreyes.scannerapp.models.InventarioActivo;
import ni.com.jdreyes.scannerapp.models.TipoInventario;
import ni.com.jdreyes.scannerapp.rest.conf.RetrofitFactory;
import ni.com.jdreyes.scannerapp.rest.service.InventarioActivoService;
import ni.com.jdreyes.scannerapp.rest.service.TipoInventarioService;
import ni.com.jdreyes.scannerapp.utils.CatalogCallBack;
import ni.com.jdreyes.scannerapp.utils.cons.ActivityRequestCode;

public class MainActivity extends AppCompatActivity implements CatalogCallBack, ActivityRequestCode {

    //private Fragment despacho, carga, bodega;
    private final TipoInventarioService tipoInventarioService =  RetrofitFactory.createService(TipoInventarioService.class);
    private final InventarioActivoService inventarioActivoService =  RetrofitFactory.createService(InventarioActivoService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        despacho = ProduccionInvFragment.newInstance();
//        carga = CargaInvFragment.newInstance();
//        bodega = BodegaFragment.newInstance();

        //Activity onBackEvent
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(getApplicationContext(), "No puede ir mas atras", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        Button buttonScanner = findViewById(R.id.btnScan);
        buttonScanner.setOnClickListener(this::onClickScan);

        //Combo de inventario activo
        Spinner spinnerInventarioActivo = findViewById(R.id.spinnerInventarioActivo);
        spinnerInventarioActivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                InventarioActivo tipoInventario = (InventarioActivo) adapterView.getSelectedItem();
                ((TextView)findViewById(R.id.txtIdInv)).setText(tipoInventario.getId_Inventario().toString());
                ((TextView)findViewById(R.id.txtFecha)).setText(tipoInventario.getFecha());
                ((TextView)findViewById(R.id.txtBodInventario)).setText(tipoInventario.getBodega_Inventario());
                ((TextView)findViewById(R.id.txtBodegaOrigen)).setText(tipoInventario.getBodega_Destino());
                ((TextView)findViewById(R.id.txtNombreBodDestino)).setText(tipoInventario.getNombre_Bod_Destino());
                buttonScanner.setEnabled(true);
                Toast.makeText(getApplicationContext(), tipoInventario.getId_Inventario() + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Combo de Tipo de inventario
        Spinner spinnerTipo = findViewById(R.id.spinnerTipoInventario);
        //Evento de seleccion de tipo inventario
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TipoInventario tipoInventario = (TipoInventario) adapterView.getSelectedItem();
                Toast.makeText(getApplicationContext(), tipoInventario.getId_tipo_inventario() + "", Toast.LENGTH_LONG).show();
                inventarioActivoService.obtenerInventarioActivo(tipoInventario.getId_tipo_inventario())
                        .enqueue(catalogCallbackList(
                            InventarioActivoAdapter.class,
                            getApplicationContext(),
                            spinnerInventarioActivo
                        )
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tipoInventarioService.obtenerTipoInventario().enqueue(catalogCallbackList(
                TipoInventarioAdapter.class,
                getApplicationContext(),
                spinnerTipo
                )
        );


    }

    //Evento de click en el boton de scanear
    public void onClickScan(View view) {
        Toast.makeText(getApplicationContext(), "evento", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == -1) {
                // Get String data from Intentm
                String returnString = data.getStringExtra("BARCODE");
                Toast.makeText(getApplicationContext(), String.format("Codigo: %s", returnString), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "Error al escanear", Toast.LENGTH_SHORT).show();
            }
        }
    }
}