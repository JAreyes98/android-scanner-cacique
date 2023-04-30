package ni.com.jdreyes.scannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ni.com.jdreyes.scannerapp.databinding.ActivityScannerBinding;
import ni.com.jdreyes.scannerapp.utils.ImageAnalizer;

public class ScannerActivity extends AppCompatActivity {


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private FloatingActionButton btnInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivityScannerBinding.inflate(getLayoutInflater()).getRoot());

        //Init cameraX
        init();

        //Button
        btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setVisibility(View.INVISIBLE);
        btnInfo.setOnClickListener(onBtnInfoClickListener);
    }

    private final View.OnClickListener onBtnInfoClickListener = v -> {
        Intent intent = new Intent(v.getContext(), ProductoActivity.class);
        startActivityForResult(intent, 0);
    };

    private void init() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFeature = ProcessCameraProvider.getInstance(this);

        OnSuccessListener<List<Barcode>> onSuccessListener =
                barcodes -> {
//                    readerBarcodeData(barcodes);
                    if (!barcodes.isEmpty()) {
                        Intent intent = new Intent();
                        int RESULT;
                        try {
                                executorService.shutdown();

                            Barcode barcode = barcodes.get(0);
                            Toast.makeText(this, "Codigo: ".concat(barcode.getRawValue()), Toast.LENGTH_LONG)
                                    .show();
                            cameraProviderFeature.get().unbindAll();
                            intent.putExtra("BARCODE", barcode.getRawValue());
                            RESULT = RESULT_OK;

                            btnInfo.setVisibility(View.VISIBLE);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                            RESULT = RESULT_CANCELED;
                        }
                        setResult(RESULT, intent);
//                        finish();
                    }
                };
        OnFailureListener onFailureListener = Throwable::printStackTrace;
        ImageAnalizer imageAnalizer =
                new ImageAnalizer(getSupportFragmentManager(), onSuccessListener, onFailureListener);
        PreviewView view = findViewById(R.id.preview_view);

        cameraProviderFeature.addListener(
                () -> {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFeature.get();
                        startCameraX(cameraProvider, view, imageAnalizer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                ContextCompat.getMainExecutor(this));
    }

    private void startCameraX(ProcessCameraProvider cameraProvider, PreviewView view, ImageAnalizer imageAnalizer) {
        CameraSelector selector =
                new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(view.getSurfaceProvider());

        // Image capture use case
        ImageCapture imageCapture = new ImageCapture.Builder().build();

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1200, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
        imageAnalysis.setAnalyzer(executorService, imageAnalizer);

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, selector, preview, imageCapture, imageAnalysis);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                init();
                btnInfo.setVisibility(View.INVISIBLE);
            }else {
                Toast.makeText(this, "Error al escanear", Toast.LENGTH_SHORT).show();
            }
        }
    }
//    private void readerBarcodeData(List<Barcode> barcodes) {
//        for (Barcode barcode : barcodes) {
//            Rect bounds = barcode.getBoundingBox();
//            Point[] corners = barcode.getCornerPoints();
//            String rawValue = barcode.getRawValue();
//
//            int valueType = barcode.getValueType();
//
//            switch (valueType) {
//                case Barcode.TYPE_WIFI: {
//                    String ssid = barcode.getWifi().getSsid();
//                    String password = barcode.getWifi().getPassword();
//                    int type = barcode.getWifi().getEncryptionType();
//                    break;
//                }
//                case Barcode.TYPE_URL: {
//                    String tittle = barcode.getUrl().getTitle();
//                    String url = barcode.getUrl().getUrl();
//                    break;
//                }
//            }
//        }
//    }
}