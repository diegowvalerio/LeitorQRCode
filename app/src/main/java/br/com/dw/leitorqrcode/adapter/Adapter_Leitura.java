package br.com.dw.leitorqrcode.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.dw.leitorqrcode.R;
import br.com.dw.leitorqrcode.entidade.Leitura;

public class Adapter_Leitura extends BaseAdapter {
    Context context;
    List<Leitura> leituras;

    public Adapter_Leitura(Context context, List<Leitura> leituras){
        this.context = context;
        this.leituras = leituras;
    }
    @Override
    public int getCount() {
        return leituras.size();
    }

    @Override
    public Object getItem(int position) {
        return leituras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return leituras.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adp_leitura,parent,false);
        final Leitura leitura = leituras.get(position);

        ImageButton btncopiar,btncompartilhar,btnlink;
        btncopiar = view.findViewById(R.id.adp_btncopiar);
        btncompartilhar = view.findViewById(R.id.adp_btncompartilhar);
        btnlink = view.findViewById(R.id.adp_btnlink);
        btnlink.setVisibility(View.INVISIBLE);
        TextView nleitura = view.findViewById(R.id.adp_leitura);
        TextView ndata = view.findViewById(R.id.adp_dataleitura);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        btncopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leitura.getLeitura().length()>0) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", "" + leitura.getLeitura());
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(context, "Conteúdo Copiado com Sucesso ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Não há dados para Copiar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btncompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, leitura.getLeitura());
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent,"Leitor QR Code"));
            }
        });

        if(URLUtil.isValidUrl(leitura.getLeitura())){
            btnlink.setVisibility(View.VISIBLE);
            btnlink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(leitura.getLeitura()));
                    context.startActivity(i);
                }
            });
        }

        nleitura.setText(leitura.getLeitura());
        ndata.setText(sdf.format(leitura.getData_leitura()));
        view.setBackgroundResource(R.drawable.borda);
        return view;
    }

}
