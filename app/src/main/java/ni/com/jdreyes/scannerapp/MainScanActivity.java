package ni.com.jdreyes.scannerapp;

import static ni.com.jdreyes.scannerapp.utils.cons.ActivityRequestCode.SECOND_ACTIVITY_REQUEST_CODE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ni.com.jdreyes.scannerapp.data.MyDatabaseHelper;
import ni.com.jdreyes.scannerapp.utils.ChekingPermission;
import ni.com.jdreyes.scannerapp.utils.ScannedItem;
import ni.com.jdreyes.scannerapp.utils.ScannedItemAdapter;

public class MainScanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScannedItemAdapter adapter;
    private List<ScannedItem> itemList = new ArrayList<>();

    private MenuItem bulkMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scan);


        recyclerView = findViewById(R.id.barcodeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadScannedItems(); // Populate the list
        focusInput();
        Button buttonScanner = findViewById(R.id.btnScan);
        buttonScanner.setEnabled(true);
        buttonScanner.setOnClickListener(this::onClickScan);
        setupAutoScan();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ChekingPermission.checkPermissions(this);
        }
    }

    private void focusInput(){
        EditText input = findViewById(R.id.inputBarCode);
        input.requestFocus();
    }

    private void setupAutoScan() {
        EditText input = findViewById(R.id.inputBarCode);

        // Opción 1: detectar Enter físico
        input.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                String scannedBarcode = input.getText().toString().trim();
                processScanned(scannedBarcode);
                input.setText("");
                return true;
            }
            return false;
        });

        // Opción 2: por si el Enter llega como \n
        input.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                // si DataWedge manda un salto de línea al final
                if (s.length() > 0 && s.charAt(s.length()-1) == '\n') {
                    String code = s.toString().trim();
                    processScanned(code);
                    s.clear();
                }
            }
        });
    }

    private void processScanned(String scannedBarcode) {
        if (scannedBarcode.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No hay un código de barra presente", Toast.LENGTH_LONG).show();
            focusInput();
            cleanInput();
            return;
        }
        if (itemList.stream().anyMatch(item -> item.getBarcode().equalsIgnoreCase(scannedBarcode))) {
            Toast.makeText(getApplicationContext(), "Validación -> Código ya ha sido agregado", Toast.LENGTH_LONG).show();
            focusInput();
            cleanInput();
            return;
        }
        insertScannedCode(this, scannedBarcode);
        Toast.makeText(getApplicationContext(), "Código agregado correctamente", Toast.LENGTH_LONG).show();
        focusInput();
        cleanInput();

    }

    private void cleanInput(){
        EditText input = findViewById(R.id.inputBarCode);
        input.setText("");
    }



    public void onClickScan(View view) {
//        Toast.makeText(getApplicationContext(), "evento", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
//        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
        EditText input = findViewById(R.id.inputBarCode);
        String scannedBarcode = input.getText().toString();
        processScanned(scannedBarcode);
    }


    private void loadScannedItems() {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, barcode, timestamp FROM scanned_items ORDER BY id asc", null);
        System.out.println("Loading..");
        itemList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String barcode = cursor.getString(1);
            String timestamp = cursor.getString(2);
            itemList.add(new ScannedItem(id, barcode, timestamp));
            System.out.println("Barcode: " + barcode);
        }
        cursor.close();
        db.close();

        adapter = new ScannedItemAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        bulkMenuItem = menu.findItem(R.id.menu_bulk);
        updateBulkMenuState(); // ✅ Initial state check
        return true;
    }

    private void updateBulkMenuState() {
        boolean hasInternet = isInternetAvailable();
        boolean hasItems = !itemList.isEmpty(); // your list variable

        if (bulkMenuItem != null) {
            bulkMenuItem.setEnabled(hasInternet && hasItems);
            bulkMenuItem.setVisible(hasInternet && hasItems); // optional
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_bulk) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == -1) {
                // Get String data from Intentm
                String scannedBarcode = data.getStringExtra("BARCODE");
                insertScannedCode(this, scannedBarcode);

                Toast.makeText(getApplicationContext(), String.format("Codigo: %s", scannedBarcode), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "Error al escanear", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void insertScannedCode(Context context, String barcode) {
        System.out.println("Saving data: " + barcode);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("barcode", barcode);
        values.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

        db.insert("scanned_items", null, values);
        db.close();
        System.out.println("Saved: " + barcode);
        loadScannedItems();
        updateBulkMenuState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScannedItems();
        updateBulkMenuState();
    }

}