package ni.com.jdreyes.scannerapp.fragments.despacho;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import ni.com.jdreyes.scannerapp.R;
import ni.com.jdreyes.scannerapp.ScannerActivity;
import ni.com.jdreyes.scannerapp.rest.conf.RetrofitFactory;
import ni.com.jdreyes.scannerapp.rest.service.CatalogService;
import ni.com.jdreyes.scannerapp.rest.service.InventoryService;
import ni.com.jdreyes.scannerapp.utils.CatalogCallBack;
import ni.com.jdreyes.scannerapp.utils.ThrowableUtils;
import ni.com.jdreyes.scannerapp.utils.cons.ActivityRequestCode;

public class ProduccionInvFragment extends Fragment implements ActivityRequestCode, ThrowableUtils, CatalogCallBack {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produccion_inv, container, false);

        view.findViewById(R.id.btnVerificarCarga).setOnClickListener(this::onClickVerificarProd);
        view.findViewById(R.id.btnScanCarga).setOnClickListener(this::onClickScanProd);
        return view;
    }

    public void onClickVerificarProd(View view) {
        Toast.makeText(getContext(), "Ok", Toast.LENGTH_SHORT).show();
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
                ((EditText)getActivity().findViewById(R.id.editCodigoProdInv)).setText(returnString);
            }else {
                Toast.makeText(getContext(), "Error al escanear", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadCatalogs() {
//        catalogService.fetchCamiones().enqueue(catalogCallback(CamionAdapter.class, getContext(), binding.spinnerCamion));
//        catalogService.fetchOrdenesCarga().enqueue(catalogCallback(OrdenesAdapter.class, getContext(), binding.spinnerOrdenCarga));
    }
}