package br.com.dw.leitorqrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.sql.SQLException;
import java.util.Date;

import br.com.dw.leitorqrcode.banco.DatabaseHelper;
import br.com.dw.leitorqrcode.entidade.Leitura;
import br.com.dw.leitorqrcode.dao.Dao_Leitura;

public class Resultado extends AppCompatActivity {

    EditText resultado;
    DatabaseHelper banco;
    Leitura leitura = new Leitura();
    Dao_Leitura dao_leitura;
    ImageButton btnlink;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        mAdView = findViewById(R.id.adView);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        });

        btnlink = findViewById(R.id.btnlink);
        btnlink.setVisibility(View.INVISIBLE);
        resultado = findViewById(R.id.editresultado);
        resultado.setEnabled(false);
        banco = new DatabaseHelper(this);
        try {
            dao_leitura = new Dao_Leitura(banco.getConnectionSource());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        salvaresultador();
    }

    public void salvaresultador(){
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("resultado")) {
            String v =  bundle.getString("resultado");
            if(v.length()>0){
                try {
                resultado.setText(v);
                leitura.setLeitura(v);
                leitura.setData_leitura(new Date());
                dao_leitura.createIfNotExists(leitura);
                if(URLUtil.isValidUrl(v)){
                    btnlink.setVisibility(View.VISIBLE);
                    btnlink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(resultado.getText().toString()));
                            startActivity(i);
                        }
                    });
                }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao salvar Leitura", Toast.LENGTH_SHORT).show();
                }
            }else{
                resultado.setText("");
            }
        }else{
            Toast.makeText(this, "Erro na Leitura", Toast.LENGTH_SHORT).show();
        }
    }

    public void copiar(View view){
        if (resultado.getText().length()>0) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", "" + resultado.getText());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Conteúdo Copiado com Sucesso ", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Não há dados para Copiar", Toast.LENGTH_SHORT).show();
        }
    }

    public void compartilhar(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, resultado.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,"Leitor QR Code"));
    }


}