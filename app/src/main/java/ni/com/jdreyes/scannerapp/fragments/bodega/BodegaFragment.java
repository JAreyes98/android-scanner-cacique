package ni.com.jdreyes.scannerapp.fragments.bodega;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import ni.com.jdreyes.scannerapp.R;
import ni.com.jdreyes.scannerapp.ScannerActivity;
import ni.com.jdreyes.scannerapp.utils.cons.ActivityRequestCode;

public class BodegaFragment extends Fragment implements ActivityRequestCode {


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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bodega, container, false);
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
}