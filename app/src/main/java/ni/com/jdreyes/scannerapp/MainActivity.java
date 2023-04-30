package ni.com.jdreyes.scannerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ni.com.jdreyes.scannerapp.utils.ChekingPermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLogin).setOnClickListener(onClickLogin());
    }

    public View.OnClickListener onClickLogin() {
        return v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ChekingPermission.checkPermissions(this);
                return;
            }

            Toast.makeText(MainActivity.this, "Clicking login", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), ScannerActivity.class);
                startActivity(intent);
        };
    }
}