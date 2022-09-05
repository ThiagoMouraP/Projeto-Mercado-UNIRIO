package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Favoritado extends AppCompatActivity {
      ArrayList<Usuario> pessoa = new ArrayList<>();
      ArrayList<String> nome = new ArrayList<>();
      private ArrayList<Drawable> imagem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritado);
        Intent intent = getIntent();
        final Anuncio produto = (Anuncio) intent.getSerializableExtra("nome");
        final Intent intent2 = new Intent(this, GerenciarVendas.class);
        nome.add("Voltar");
        imagem.add(getResources().getDrawable(R.drawable.esquerda));
        preencher(produto);
        ListView lista = (ListView) findViewById(R.id.line1);
        CustomListAdapter adapter=new CustomListAdapter(this, nome, imagem);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nome );
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    startActivity(intent2);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + pessoa.get(position-1).getTelefone() +
                            "&text=" + "Olá,me chamo " + Principal.retornaUsuario().getNome()));
                    startActivity(intent);
                }
            }
        });
    }
    public  void preencher(Anuncio produto){
        for(int i = 0; i < produto.getFavoritado().size(); i++){
            pessoa.add(produto.getFavoritado().get(i));
            nome.add(produto.getFavoritado().get(i).toString());
            if(produto.getFavoritado().get(i).isTemImagem()) {
                Drawable drawable =
                        new BitmapDrawable(getResources(), GerenciarVendas.StringToBitMap(produto.getFavoritado().get(i).getImagem()));
                imagem.add(drawable);
            }
            else{
                imagem.add(getResources().getDrawable(R.drawable.duvida));
            }
        }
    }


}
