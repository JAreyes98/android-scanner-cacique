package ni.com.jdreyes.scannerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import ni.com.jdreyes.scannerapp.data.MyDatabaseHelper;
import ni.com.jdreyes.scannerapp.fragments.adapters.InventarioActivoAdapter;
import ni.com.jdreyes.scannerapp.fragments.adapters.TipoInventarioAdapter;
import ni.com.jdreyes.scannerapp.models.BulkItem;
import ni.com.jdreyes.scannerapp.models.BulkRequest;
import ni.com.jdreyes.scannerapp.models.InventarioActivo;
import ni.com.jdreyes.scannerapp.models.TipoInventario;
import ni.com.jdreyes.scannerapp.models.wrapper.DataWrapper;
import ni.com.jdreyes.scannerapp.rest.conf.RetrofitFactory;
import ni.com.jdreyes.scannerapp.rest.service.InventarioActivoService;
import ni.com.jdreyes.scannerapp.rest.service.InventoryService;
import ni.com.jdreyes.scannerapp.rest.service.TipoInventarioService;
import ni.com.jdreyes.scannerapp.utils.CatalogCallBack;
import ni.com.jdreyes.scannerapp.utils.ChekingPermission;
import ni.com.jdreyes.scannerapp.utils.ScannedItem;
import ni.com.jdreyes.scannerapp.utils.ScannedItemAdapter;
import ni.com.jdreyes.scannerapp.utils.cons.ActivityRequestCode;
import ni.com.jdreyes.scannerapp.utils.enums.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CatalogCallBack, ActivityRequestCode {

    //private Fragment despacho, carga, bodega;
    private final TipoInventarioService tipoInventarioService =  RetrofitFactory.createService(TipoInventarioService.class);
    private final InventarioActivoService inventarioActivoService =  RetrofitFactory.createService(InventarioActivoService.class);
    private final InventoryService inventoryService =  RetrofitFactory.createService(InventoryService.class);

    private InventarioActivo inventarioActivo;
    private TipoInventario tipoInventario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonBulk = findViewById(R.id.btnBulk);
        buttonBulk.setOnClickListener(this::onClickBulk);

        //Combo de inventario activo
        Spinner spinnerInventarioActivo = findViewById(R.id.spinnerInventarioActivo);
        spinnerInventarioActivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inventarioActivo = (InventarioActivo) adapterView.getSelectedItem();
                buttonBulk.setEnabled(true);
                Toast.makeText(getApplicationContext(), inventarioActivo.getId_Inventario() + "", Toast.LENGTH_LONG).show();
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
                tipoInventario = (TipoInventario) adapterView.getSelectedItem();
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
    public void onClickBulk(View view) {
        Toast.makeText(getApplicationContext(), "El proceso de volcado esta comenzando...", Toast.LENGTH_LONG).show();
        List<BulkItem> items = new ArrayList<>();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, barcode, timestamp FROM scanned_items ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String barcode = cursor.getString(1);
            String timestamp = cursor.getString(2);
            items.add(new BulkItem(barcode));
        }

        BulkRequest request = new BulkRequest();
        request.setTipoInventario(tipoInventario.getId_tipo_inventario());
        request.setInventarioActivo(inventarioActivo.getId_Inventario().intValue());
        request.setItems(items);
        inventoryService.bulkData(request).enqueue(new Callback<DataWrapper<String>>() {
            @Override
            public void onResponse(Call<DataWrapper<String>> call, Response<DataWrapper<String>> response) {
                HttpStatus httpStatus = HttpStatus.resolve(response.code());
                if (response.isSuccessful() && httpStatus == HttpStatus.OK) {
                    Toast.makeText(getApplicationContext(), "Volcado ha sido exitoso", Toast.LENGTH_LONG).show();
                    dbHelper.onUpgrade(db, 0,0);

                    cursor.close();
                    db.close();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "El volcado ha fallado", Toast.LENGTH_LONG).show();
                    System.out.println(response.body());

                    cursor.close();
                    db.close();
                }
            }

            @Override
            public void onFailure(Call<DataWrapper<String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al hacer el volcado", Toast.LENGTH_LONG).show();
                cursor.close();
                db.close();
            }
        });

    }
}