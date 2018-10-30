package com.example.noahliu.super_ver2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView ScanenerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        ScanenerView = new ZXingScannerView(this);
        setContentView(ScanenerView);

    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();

        ScanenerView.stopCamera();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ScanenerView.setResultHandler(this);
        ScanenerView.startCamera();
    }
}
