package ni.com.jdreyes.scannerapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ProductoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

//        ((EditText)findViewById(R.id.editCodigoProd)).setText(savedInstanceState.getString("codproduct"));
//        ((EditText)findViewById(R.id.editNombreProd)).setText(savedInstanceState.getString("name"));
//        ((EditText)findViewById(R.id.editUltimaModificacion)).setText(savedInstanceState.getString("lastUpdate"));

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                setResult(RESULT_OK);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    /**
     *
     setResult(RESULT, intent);
     finish();
    * */
}