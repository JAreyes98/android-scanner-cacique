package ni.com.jdreyes.scannerapp.fragments.bodega;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ni.com.jdreyes.scannerapp.R;
import ni.com.jdreyes.scannerapp.ScannerActivity;
import ni.com.jdreyes.scannerapp.databinding.FragmentBodegaBinding;
import ni.com.jdreyes.scannerapp.databinding.FragmentCargaInvBinding;
import ni.com.jdreyes.scannerapp.fragments.adapters.BodegaAdapter;
import ni.com.jdreyes.scannerapp.fragments.adapters.CamionAdapter;
import ni.com.jdreyes.scannerapp.fragments.adapters.InventarioAdapter;
import ni.com.jdreyes.scannerapp.fragments.adapters.OrdenesAdapter;
import ni.com.jdreyes.scannerapp.models.Bodega;
import ni.com.jdreyes.scannerapp.models.Camion;
import ni.com.jdreyes.scannerapp.models.Inventario;
import ni.com.jdreyes.scannerapp.models.InventarioBodega;
import ni.com.jdreyes.scannerapp.models.wrapper.DataWrapper;
import ni.com.jdreyes.scannerapp.rest.conf.RetrofitFactory;
import ni.com.jdreyes.scannerapp.rest.service.CatalogService;
import ni.com.jdreyes.scannerapp.rest.service.InventoryService;
import ni.com.jdreyes.scannerapp.utils.CatalogCallBack;
import ni.com.jdreyes.scannerapp.utils.DatePickerListener;
import ni.com.jdreyes.scannerapp.utils.ThrowableUtils;
import ni.com.jdreyes.scannerapp.utils.cons.ActivityRequestCode;
import ni.com.jdreyes.scannerapp.utils.enums.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BodegaFragment extends Fragment implements ActivityRequestCode, ThrowableUtils, CatalogCallBack, DatePickerListener {

    private FragmentBodegaBinding binding;

    private final CatalogService catalogService = RetrofitFactory.createService(CatalogService.class);
    private final InventoryService inventoryService = RetrofitFactory.createService(InventoryService.class);

    // TODO: Rename and change types and number of parameters
    public static BodegaFragment newInstance() {
        return new BodegaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBodegaBinding.inflate(inflater, container, false);

        //Events binding
        binding.editFechaInvCarga.setOnClickListener(this::onClickFecha);
        View view = inflater.inflate(R.layout.fragment_bodega, container, false);
        binding.btnVerificarCarga.setOnClickListener(this::onClickVerificarProd);
        binding.btnScanCarga.setOnClickListener(this::onClickScanProd);

        binding.spinnerPlanta.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Bodega bodega = (Bodega) adapterView.getSelectedItem();
                        findInventariosPorBodega(bodega.getCodigo());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });

        //Loading ...
        loadCatalogs();

        return binding.getRoot();
    }

    public void onClickVerificarProd(View view) {
        try {
            validate();

            String fecha = binding.editFechaInvCarga.getText().toString();

            InventarioBodega bodegaInv = new InventarioBodega();
            bodegaInv.setDate(new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("dd/MM/yyyy").parse(fecha)));
            bodegaInv.setBarcode(binding.editCodigoProdInv.getText().toString());
            bodegaInv.setBodega((Bodega) binding.spinnerPlanta.getSelectedItem());
            bodegaInv.setIdInventario(((Inventario) binding.spinnerInventario.getSelectedItem()).getId());

            inventoryService.checkWarehouseInventory(bodegaInv).enqueue(new Callback<DataWrapper<String>>() {
                @Override
                public void onResponse(Call<DataWrapper<String>> call, Response<DataWrapper<String>> response) {
                    HttpStatus httpStatus = HttpStatus.resolve(response.code());
                    if (response.isSuccessful() && httpStatus == HttpStatus.OK && response.body().getData().equals("correcto")) {
                        binding.editMensajeProdInv.setText("Producto encontrado");
                    }else {
                        binding.editMensajeProdInv.setText("No se encuentra");
                    }
                }

                @Override
                public void onFailure(Call<DataWrapper<String>> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onClickScanProd(View view) {
        Intent intent = new Intent(getContext(), ScannerActivity.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String returnString = data.getStringExtra("BARCODE");
                binding.editCodigoProdInv.setText(returnString);
                binding.btnVerificarCarga.setVisibility(View.VISIBLE);
            }else {
                Toast.makeText(getContext(), "Error al escanear", Toast.LENGTH_SHORT).show();
                binding.btnVerificarCarga.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void loadCatalogs() {
        catalogService.fetchBodegas().enqueue(catalogCallback(BodegaAdapter.class, getContext(), binding.spinnerPlanta));
    }

    private void findInventariosPorBodega(String bodega) {
        catalogService.fetchInvetarioByWarehouse(bodega).enqueue(catalogCallback(InventarioAdapter.class, getContext(), binding.spinnerInventario));
    }

    private void validate() {
        throwIf(StringUtils::isEmpty, binding.editFechaInvCarga.getText().toString(), "Fecha: no se ha especificado");
        throwIf(Objects::isNull, binding.spinnerPlanta.getSelectedItem().toString(), "Camion: no se ha especificado");
        throwIf(Objects::isNull, binding.spinnerInventario.getSelectedItem().toString(), "Inventario: no se ha especificado");
        throwIf(StringUtils::isEmpty, binding.editCodigoProdInv.getText().toString(), "El producto no ha sido escaneado");
    }

    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Date date = new Date(i - 1900, i1, i2);
        String dateFormatter = new SimpleDateFormat("dd/MM/yyyy").format(date);
        binding.editFechaInvCarga.setText(dateFormatter);
    }
}