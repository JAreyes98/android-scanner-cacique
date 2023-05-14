package ni.com.jdreyes.scannerapp.fragments.cargacamion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import ni.com.jdreyes.scannerapp.R;
import ni.com.jdreyes.scannerapp.ScannerActivity;
import ni.com.jdreyes.scannerapp.databinding.FragmentCargaInvBinding;
import ni.com.jdreyes.scannerapp.fragments.adapters.CamionAdapter;
import ni.com.jdreyes.scannerapp.fragments.adapters.OrdenesAdapter;
import ni.com.jdreyes.scannerapp.models.Camion;
import ni.com.jdreyes.scannerapp.models.OrdenCarga;
import ni.com.jdreyes.scannerapp.models.VerificarOrden;
import ni.com.jdreyes.scannerapp.models.wrapper.DataWrapper;
import ni.com.jdreyes.scannerapp.rest.conf.RetrofitFactory;
import ni.com.jdreyes.scannerapp.rest.service.CatalogService;
import ni.com.jdreyes.scannerapp.rest.service.InventoryService;
import ni.com.jdreyes.scannerapp.utils.CatalogCallBack;
import ni.com.jdreyes.scannerapp.utils.ThrowableUtils;
import ni.com.jdreyes.scannerapp.utils.cons.ActivityRequestCode;
import ni.com.jdreyes.scannerapp.utils.enums.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CargaInvFragment extends Fragment implements ActivityRequestCode, ThrowableUtils, CatalogCallBack {

    private FragmentCargaInvBinding binding;
    private final CatalogService catalogService = RetrofitFactory.createService(CatalogService.class);
    private final InventoryService inventoryService = RetrofitFactory.createService(InventoryService.class);

    public static CargaInvFragment newInstance() {
        return new CargaInvFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCargaInvBinding.inflate(inflater, container, false);

        //Events binding
        binding.editFechaInvCarga.setOnClickListener(this::onClickFecha);
        binding.btnScanCarga.setOnClickListener(this::onClickScanProd);
        binding.btnVerificarCarga.setOnClickListener(this::onClickVerificarProd);

        //Loading ...
        loadCatalogs();

        return binding.getRoot();
    }

    public void onClickVerificarProd(View view) {
        try {
            validate();
            String fecha = binding.editFechaInvCarga.getText().toString();
            Camion camion = (Camion) binding.spinnerCamion.getSelectedItem();
            OrdenCarga ordenCarga = (OrdenCarga) binding.spinnerOrdenCarga.getSelectedItem();
            String barcode = binding.editCodigoProdInv.getText().toString();

            VerificarOrden orden = new VerificarOrden();
            orden.setDate(new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("dd/MM/yyyy").parse(fecha)));
            orden.setCamion(camion);
            orden.setOrderId(orden.getOrderId());
            orden.setBarcode(barcode);

            inventoryService.checkOrder(orden).enqueue(new Callback<DataWrapper<String>>() {
                @Override
                public void onResponse(Call<DataWrapper<String>> call, Response<DataWrapper<String>> response) {
                    HttpStatus httpStatus = HttpStatus.resolve(response.code());
                    if (response.isSuccessful() && httpStatus == HttpStatus.OK && response.body().getData().equals("correcto")) {
                        binding.editMensajeProdInv.setText("Producto encontrado");
//                        binding.editMensajeProdInv.setTextColor(255);
                    }else {
                        binding.editMensajeProdInv.setText("No se encuentra");
//                        binding.editMensajeProdInv.setTextColor(255);
                    }
                }

                @Override
                public void onFailure(Call<DataWrapper<String>> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onClickScanProd(View view) {
        Intent intent = new Intent(getContext(), ScannerActivity.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    public void onClickFecha(View view) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog =
                new DatePickerDialog(getContext(), R.style.Theme_Scannerapp, this::onDateSet, year, month, day);
        dialog.show();
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

    private void validate() {
        throwIf(StringUtils::isEmpty, binding.editFechaInvCarga.getText().toString(), "Fecha: no se ha especificado");
        throwIf(Objects::isNull, binding.spinnerCamion.getSelectedItem().toString(), "Camion: no se ha especificado");
        throwIf(Objects::isNull, binding.spinnerOrdenCarga.getSelectedItem(), "Orden carga: no se ha especificado");
        throwIf(StringUtils::isEmpty, binding.editCodigoProdInv.getText().toString(), "El producto no ha sido escaneado");
    }

    private void loadCatalogs() {
        catalogService.fetchCamiones().enqueue(catalogCallback(CamionAdapter.class, getContext(), binding.spinnerCamion));
        catalogService.fetchOrdenesCarga().enqueue(catalogCallback(OrdenesAdapter.class, getContext(), binding.spinnerOrdenCarga));
    }



    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Date date = new Date(i - 1900, i1, i2);
        String dateFormatter = new SimpleDateFormat("dd/MM/yyyy").format(date);
        binding.editFechaInvCarga.setText(dateFormatter);

    }
}