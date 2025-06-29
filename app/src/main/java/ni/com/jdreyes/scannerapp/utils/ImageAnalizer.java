package ni.com.jdreyes.scannerapp.utils;

import android.annotation.SuppressLint;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class ImageAnalizer implements ImageAnalysis.Analyzer {

  private final FragmentManager fragmentManager;
  private final OnSuccessListener onSuccessListener;
  private final OnFailureListener onFailureListener;

  public ImageAnalizer(
      FragmentManager fragmentManager,
      OnSuccessListener onSuccessListener,
      OnFailureListener onFailureListener) {
    this.fragmentManager = fragmentManager;
    this.onSuccessListener = onSuccessListener;
    this.onFailureListener = onFailureListener;
  }

  @Override
  public void analyze(@NonNull ImageProxy image) {
    scanBarcode(image);
  }

  private void scanBarcode(ImageProxy imageProxy) {
    @SuppressLint("UnsafeOptInUsageError")
    Image image = imageProxy.getImage();

    if (image == null) return;

    InputImage ii =
        InputImage.fromMediaImage(image, imageProxy.getImageInfo().getRotationDegrees());
    BarcodeScannerOptions options =
        new BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
//                Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_CODE_128,
                    Barcode.FORMAT_ALL_FORMATS
//                Barcode.FORMAT_CODABAR,
//                Barcode.FORMAT_EAN_13,
//                Barcode.FORMAT_PDF417
            )
            .build();

        BarcodeScanning.getClient(options)
            .process(ii)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)
        .addOnCompleteListener(
                (OnCompleteListener<List<Barcode>>) task -> imageProxy.close());
  }
}
