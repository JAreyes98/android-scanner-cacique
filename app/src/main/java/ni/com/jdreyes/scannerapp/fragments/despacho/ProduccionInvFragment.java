package ni.com.jdreyes.scannerapp.fragments.despacho;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ni.com.jdreyes.scannerapp.R;
import ni.com.jdreyes.scannerapp.ScannerActivity;
import ni.com.jdreyes.scannerapp.databinding.FragmentProduccionInvBinding;
import ni.com.jdreyes.scannerapp.fragments.adapters.InventarioAdapter;
import ni.com.jdreyes.scannerapp.fragments.adapters.PlantaAdapter;
import ni.com.jdreyes.scannerapp.models.Bodega;
import ni.com.jdreyes.scannerapp.models.Inventario;
import ni.com.jdreyes.scannerapp.models.Planta;
import ni.com.jdreyes.scannerapp.models.VerificarInventarioProduccion;
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

public class ProduccionInvFragment extends Fragment implements ActivityRequestCode, ThrowableUtils, CatalogCallBack, DatePickerListener {

    FragmentProduccionInvBinding binding;
    private final CatalogService catalogService = RetrofitFactory.createService(CatalogService.class);
    private final InventoryService inventoryService = RetrofitFactory.createService(InventoryService.class);

    public static ProduccionInvFragment newInstance() {
        return new ProduccionInvFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProduccionInvBinding.inflate(inflater, container, false);

        //Events
        binding.editFechaInvProc.setOnClickListener(this::onClickFecha);
        binding.btnVerificarCarga.setOnClickListener(this::onClickVerificarProd);
        binding.btnScanCarga.setOnClickListener(this::onClickScanProd);

        binding.spinnerPlanta.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Planta bodega = (Planta) adapterView.getSelectedItem();
                        findInventariosPorPlanta(bodega.getPlanta());
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

            String fecha = binding.editFechaInvProc.getText().toString();

            VerificarInventarioProduccion verificar = new VerificarInventarioProduccion();
            verificar.setDate(new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("dd/MM/yyyy").parse(fecha)));
            verificar.setPlanta((Planta) binding.spinnerPlanta.getSelectedItem());
            verificar.setIdInventario(((Inventario) binding.spinnerInventario.getSelectedItem()).getId());
            verificar.setBarcode(binding.editCodigoProdInv.getText().toString());

            inventoryService.checkProductionInventory(verificar).enqueue(new Callback<DataWrapper<String>>() {
                @Override
                public void onResponse(Call<DataWrapper<String>> call, Response<DataWrapper<String>> response) {
                    HttpStatus httpStatus = HttpStatus.resolve(response.code());
                    if (response.isSuccessful() && httpStatus == HttpStatus.OK && response.body().getData().equals("correcto")) {
                        binding.editMensajeProdInv.setText("Producto encontrado");
                    } else {
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

    // This method is called when the second activity finishes
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
            } else {
                Toast.makeText(getContext(), "Error al escanear", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadCatalogs() {
        catalogService.fetchPlantas().enqueue(catalogCallback(PlantaAdapter.class, getContext(), binding.spinnerPlanta));
    }

    private void findInventariosPorPlanta(String planta) {
        catalogService.fetchInvetarioByPlant(planta).enqueue(catalogCallback(InventarioAdapter.class, getContext(), binding.spinnerInventario));
    }

    private void validate() {
        throwIf(StringUtils::isEmpty, binding.editFechaInvProc.getText().toString(), "Fecha: no se ha especificado");
        throwIf(Objects::isNull, binding.spinnerPlanta.getSelectedItem().toString(), "Planta: no se ha especificado");
        throwIf(Objects::isNull, binding.spinnerInventario.getSelectedItem().toString(), "Inventario: no se ha especificado");
    }

    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Date date = new Date(i - 1900, i1, i2);
        String dateFormatter = new SimpleDateFormat("dd/MM/yyyy").format(date);
        binding.editFechaInvProc.setText(dateFormatter);
    }
}