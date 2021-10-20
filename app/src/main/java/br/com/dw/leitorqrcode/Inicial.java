package br.com.dw.leitorqrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Inicial extends AppCompatActivity {
    Button btnleitor,btnhistorico;
    final Activity activity = this;
    AdView mAdView;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = findViewById(R.id.adView);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        });

        btnleitor = findViewById(R.id.btnleitor);
        btnhistorico = findViewById(R.id.btnhistorico);
        btnleitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leitor();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(Inicial.this,"ca-app-pub-3925364440483118/1756456371", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }

    public void leitor(){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Leitor em Execução");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            if (result.getContents() != null) {
                intent = new Intent(this, Resultado.class);
                intent.putExtra("resultado", result.getContents());
                startActivity(intent);
            } else {
                Toast.makeText(activity, "Leitor Cancelado", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void telahistorico(View view){
        Intent intent = new Intent(this, Historico.class);
        startActivity(intent);
    }
}