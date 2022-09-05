package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class informacaoProduto extends AppCompatActivity {
    private DatabaseReference database;
    private ArrayList<Usuario> user = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao_produto);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setIsIndicator(true);
        Intent intent = getIntent();
        final Anuncio produto = (Produto) intent.getSerializableExtra("nome");
        final Intent intent2 = new Intent(this ,  Avaliar.class);
        final Intent intent3 = new Intent(this ,  informacaoUsuario.class);
        ratingBar.setRating(produto.getAvaliacao());
        ArrayList<String> info = new ArrayList<>();
        info.add("Informações do produto");
        info.add(produto.toString());
        info.add("Informações do anunciante");
        info.add("Curso: "+produto.getVendedor().getCurso());
        info.add("Telefone: "+produto.getVendedor().getTelefone());
        info.add("E-mail: "+produto.getVendedor().getLogin().replace("A", "@").replace("P","."));
        info.add("Favoritar produto");
        info.add("Avaliar Produto");
        consultarUser((Produto)produto);
        final String email = info.get(5);
        final String email2 = email.substring(7, email.length());
        ListView lista = (ListView) findViewById(R.id.line1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==2){
                    intent3.putExtra("nome",user.get(0));
                    startActivity(intent3);
                }
                if(position==4){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+produto.getVendedor().getTelefone() +
                            "&text="+"Olá,me chamo "+Principal.retornaUsuario().getNome()+" e estou interessado no produto "+produto.getNome()));
                    startActivity(intent);
                }
                if(position==5){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:"+email2+"?subject=Produto do UniMarket" + "&body=Estou interessado no seu produto" );
                    intent.setData(data);
                    startActivity(intent);
                }
                if(position==6){
                    Toast.makeText(getApplicationContext(), "Favoritado", Toast.LENGTH_SHORT).show();
                    try {
                        produto.favoritar(Principal.retornaUsuario());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                if(position==7){
                    intent2.putExtra("nome",  produto);
                    startActivity(intent2);
                }
            }
        });
    }
    public void consultarUser(final Produto produto){
        database = FirebaseDatabase.getInstance().getReference("Usuario/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Usuario value = dataSnapshot.getValue(Usuario.class);
                        if((produto.getVendedor().getLogin().equals(value.getLogin()))) {
                            user.add(value);
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
}
