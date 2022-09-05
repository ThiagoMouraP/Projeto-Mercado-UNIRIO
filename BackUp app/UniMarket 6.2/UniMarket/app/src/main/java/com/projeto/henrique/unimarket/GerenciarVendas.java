package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class GerenciarVendas extends AppCompatActivity {
    ArrayList<String> nomes = new ArrayList<>();
    ArrayList<Anuncio> anuncios = new ArrayList<>();
    ArrayList<Anuncio> prod= new ArrayList<>();
    private DatabaseReference database;
    private ArrayList<Drawable> imagem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = new Intent(this, AdicionarVenda.class);
        final Intent intent2 = new Intent(this, informacoesProdutoDono.class);
        final Intent intent3 = new Intent(this, informacoesServicoDono.class);
        setContentView(R.layout.activity_gerenciar_vendas);
        nomes.add("Adicionar novo anuncio para venda");
        imagem.add(getResources().getDrawable(R.drawable.mais));
        consultarProdutos();
        consultarServicos();
        ListView lista = (ListView) findViewById(R.id.line1);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        CustomListAdapter adapter=new CustomListAdapter(this, nomes, imagem);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              if(position==0){
                  startActivity(intent);
              }
              else if(position<=prod.size()){
                  anuncios.get(position-1).setImagem("");
                  intent2.putExtra("nome", anuncios.get(position-1));
                  startActivity(intent2);
              }
              else{
                  anuncios.get(position-1).setImagem("");
                  intent3.putExtra("nome", anuncios.get(position-1));
                  startActivity(intent3);
              }
            }
        });


    }
    public void consultarProdutos(){
        database = FirebaseDatabase.getInstance().getReference("Produto/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Produto value = dataSnapshot.getValue(Produto.class);
                        if(value.getVendedor().getNome().equals(Principal.retornaUsuario().getNome())) {
                            nomes.add(value.toString());
                            anuncios.add(value);
                            prod.add(value);
                            if(value.isTemImagem()) {
                                Drawable drawable =
                                        new BitmapDrawable(getResources(), GerenciarVendas.StringToBitMap(value.getImagem()));
                                imagem.add(drawable);
                            }
                            else{
                                imagem.add(getResources().getDrawable(R.drawable.duvida));
                            }
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    public void consultarServicos(){
        database = FirebaseDatabase.getInstance().getReference("Servico/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Servico value = dataSnapshot.getValue(Servico.class);
                        if(value.getVendedor().getNome().equals(Principal.retornaUsuario().getNome())) {
                            nomes.add(value.toString());
                            anuncios.add(value);
                            if(value.isTemImagem()) {
                                Drawable drawable =
                                        new BitmapDrawable(getResources(), GerenciarVendas.StringToBitMap(value.getImagem()));
                                imagem.add(drawable);
                            }
                            else{
                                imagem.add(getResources().getDrawable(R.drawable.duvida));
                            }
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }
    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
