package com.hackupcteam.crowdify;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class InsertLocationActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler,View.OnClickListener {

    private ZXingScannerView mScannerView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_location);

        TextView tv = (TextView) findViewById(R.id.title);
        tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ProductSans-Bold.ttf"));
        intent = new Intent(getApplicationContext(), PrincipalActivity.class);
    }



    @Override
    public void handleResult(Result result) {
        //Do anything with result here :D
        Log.v("handleResult", result.getText());
        int fila = result.getText().charAt(0);
        int columna = result.getText().charAt(2);

        intent.putExtra("fila",fila);
        intent.putExtra("columna",columna);

        startActivity(intent);

        //Resume scanning
        //mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGo:
                EditText editText = (EditText) findViewById(R.id.fila);
                String fila = editText.getText().toString();
                editText = (EditText) findViewById(R.id.columna);
                String columna = editText.getText().toString();
                intent.putExtra("fila",fila);
                intent.putExtra("columna",columna);

                String behavior  = getMockBehavior();

                intent.putExtra("behavior",behavior);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonQR:
                Toast.makeText(getApplicationContext(),"QR Pressed", Toast.LENGTH_SHORT).show();
                mScannerView = new ZXingScannerView(this);
                setContentView(mScannerView);
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
                break;
        }
    }

    String getMockBehavior() {
        return  "{\n" +
                "  \"behavior\": [\n" +
                "    {\n" +
                "      \"color\": \"#FF4081\",\n" +
                "      \"flash\": \"no\",\n" +
                "      \"vibrate\": \"yes\",\n" +
                "      \"duration\": \"1000\"\n" +
                "    },\n" +
                "    {\n" +
                "     \"color\": \"#FFCDD2\",\n" +
                "      \"flash\": \"no\",\n" +
                "      \"vibrate\": \"no\",\n" +
                "      \"duration\": \"1000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"color\": \"#FFFFFF\",\n" +
                "      \"flash\": \"no\",\n" +
                "      \"vibrate\": \"yes\",\n" +
                "      \"duration\": \"1000\"\n" +
                "    },\n" +

                "    {\n" +
                "      \"color\": \"#3333EE\",\n" +
                "      \"flash\": \"no\",\n" +
                "      \"vibrate\": \"no\",\n" +
                "      \"duration\": \"1000\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
