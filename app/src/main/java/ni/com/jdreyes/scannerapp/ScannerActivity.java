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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ni.com.jdreyes.scannerapp.databinding.ActivityScannerBinding;
import ni.com.jdreyes.scannerapp.models.Producto;
import ni.com.jdreyes.scannerapp.rest.conf.RetrofitFactory;
import ni.com.jdreyes.scannerapp.rest.service.ProductService;
import ni.com.jdreyes.scannerapp.utils.ImageAnalizer;
import ni.com.jdreyes.scannerapp.utils.enums.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerActivity extends AppCompatActivity {


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private FloatingActionButton btnInfo;
    private final ProductService productService = RetrofitFactory.createService(ProductService.class);

    private Producto scannedProduct;

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
                    if (!barcodes.isEmpty()) {
                        try {
                                executorService.shutdown();

                            Barcode barcode = barcodes.get(0);
                            Toast.makeText(this, "Codigo: ".concat(barcode.getRawValue()), Toast.LENGTH_LONG)
                                    .show();
                            cameraProviderFeature.get().unbindAll();

                            btnInfo.setVisibility(View.VISIBLE);
                            productService.fetchProduct(barcode.getRawValue()).enqueue(new Callback<Producto>() {
                                @Override
                                public void onResponse(Call<Producto> call, Response<Producto> response) {
                                    HttpStatus status = HttpStatus.resolve(response.code());
                                    if (response.isSuccessful() &&  status == HttpStatus.OK){
                                        scannedProduct = response.body();
                                        Bundle bundle = new Bundle();
//                                        bundle.putInt("id", scannedProduct.getId());
//                                        bundle.putString("name", scannedProduct.getName());
//                                        bundle.putString("codproduct", scannedProduct.getCodproduct());
//                                        bundle.putString("barcode", scannedProduct.getBarcode());
//                                        bundle.putString("lastUpdate", new SimpleDateFormat("dd/MM/yyyy").format(
//                                                Objects.isNull(scannedProduct.getUpdateAt())
//                                                        ? scannedProduct.getCreateAt() : scannedProduct.getUpdateAt()
//                                        ));
                                        Intent intent = new Intent(getBaseContext(), ScannerActivity.class);
                                        startActivityForResult(intent, 0, bundle);
                                    } else {
                                        try {
                                            Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<Producto> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                                }
                            });


                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
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
}