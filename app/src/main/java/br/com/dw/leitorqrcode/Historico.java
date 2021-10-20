package br.com.dw.leitorqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.dw.leitorqrcode.adapter.Adapter_Leitura;
import br.com.dw.leitorqrcode.banco.DatabaseHelper;
import br.com.dw.leitorqrcode.dao.Dao_Leitura;
import br.com.dw.leitorqrcode.entidade.Leitura;

public class Historico extends AppCompatActivity implements AdapterView.OnItemLongClickListener{

    DatabaseHelper banco;
    Leitura leitura = new Leitura();
    List<Leitura> leituras = new ArrayList<>();
    Adapter_Leitura adapter_leitura;
    Dao_Leitura dao_leitura;

    private ListView listView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        mAdView = findViewById(R.id.adView);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        });

        banco = new DatabaseHelper(this);
        try {
            dao_leitura = new Dao_Leitura(banco.getConnectionSource());
        }catch (SQLException e){
            e.printStackTrace();
        }

        listView = findViewById(R.id.listahistorico);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            dao_leitura = new Dao_Leitura(banco.getConnectionSource());
            leituras = dao_leitura.queryBuilder().orderBy("data_leitura",false).query();
            adapter_leitura = new Adapter_Leitura(this,leituras);
            listView.setAdapter(adapter_leitura);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Leitura xleitura = (Leitura) parent.getItemAtPosition(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leitor QR Code ");
        builder.setMessage("Confirma Exclusão da Leitura ?");
        builder.setPositiveButton("Confimar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dao_leitura.delete(xleitura);
                    onStart();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
        return true;
    }
}