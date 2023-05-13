package ni.com.jdreyes.scannerapp;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ProductoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        Bundle b = getIntent().getExtras();
        ((EditText)findViewById(R.id.editCodigoProd)).setText(b.getString("codproduct"));
        ((EditText)findViewById(R.id.editNombreProd)).setText(b.getString("name"));
        ((EditText)findViewById(R.id.editUltimaModificacion)).setText(b.getString("lastUpdate"));

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