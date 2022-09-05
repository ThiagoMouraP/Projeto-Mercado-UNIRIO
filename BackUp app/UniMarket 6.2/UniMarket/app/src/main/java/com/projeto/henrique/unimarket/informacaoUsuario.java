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

public class informacaoUsuario extends AppCompatActivity {
    private DatabaseReference database;
    ArrayList<String> info = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao_usuario);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setIsIndicator(true);
        Intent intent = getIntent();
        final Usuario user = (Usuario) intent.getSerializableExtra("nome");
        final Intent intent2 = new Intent(this ,  Avaliar.class);
        ratingBar.setRating(user.getMediaAvaliacao());
        info.add("Informações da pessoa");
        info.add("Curso: "+user.getCurso());
        info.add("Telefone: "+user.getTelefone());
        info.add("E-mail: "+user.getLogin().replace("A", "@").replace("P","."));
        info.add("Avaliar anunciante");
        consultarProdutos(user);
        consultarServicos(user);
        final String email = info.get(3);
        final String email2 = email.substring(7, email.length());
        ListView lista = (ListView) findViewById(R.id.line1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==2){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+user.getTelefone() +
                            "&text="+"Olá,me chamo "+Principal.retornaUsuario()));
                    startActivity(intent);
                }
                if(position==3){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:"+email2+"?subject=Produto do UniMarket" + "&body=Estou interessado no seu produto" );
                    intent.setData(data);
                    startActivity(intent);
                }
                if(position==4){
                    intent2.putExtra("nome", user);
                    startActivity(intent2);
                }
            }
        });


    }
    public void consultarProdutos(final Usuario user){
        database = FirebaseDatabase.getInstance().getReference("Produto/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Produto value = dataSnapshot.getValue(Produto.class);
                        if(value.getVendedor().getNome().equals(user.getNome())) {
                            info.add(value.toString());
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
    public void consultarServicos(final Usuario user){
        database = FirebaseDatabase.getInstance().getReference("Servico/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Servico value = dataSnapshot.getValue(Servico.class);
                        if(value.getVendedor().getNome().equals(user.getNome())) {
                            info.add(value.toString());
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
