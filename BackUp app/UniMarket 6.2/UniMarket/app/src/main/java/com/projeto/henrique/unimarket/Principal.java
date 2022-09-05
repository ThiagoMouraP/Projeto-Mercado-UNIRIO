package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuInflater;
import android.view.Menu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Principal extends AppCompatActivity {
    private static Usuario u;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Intent intent = getIntent();
        Usuario user = (Usuario) intent.getSerializableExtra("nome");
        u = user;
        TextView n = (TextView) findViewById(R.id.apresentacao);
        String nome = user.getNome();
        String[] veri = nome.split(" ");
        if(veri.length>1){
            nome = veri[0];
        }
        String bv = "Bem vindo, "+nome;
        n.setText(bv);
        Enrolar();
        Enrolar2();
        Enrolar3();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void irGerencia(View view){
        Intent intent = new Intent(this, GerenciarVendas.class);
        startActivity(intent);
    }
    public static Usuario retornaUsuario(){
        return u;
    }
    public void Enrolar(){
        database = FirebaseDatabase.getInstance().getReference("Produto/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Produto value = dataSnapshot.getValue(Produto.class);
                        break;
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
    public void Enrolar2(){
        database = FirebaseDatabase.getInstance().getReference("Servico/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Servico value = dataSnapshot.getValue(Servico.class);
                        break;
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
    public void Enrolar3(){
        database = FirebaseDatabase.getInstance().getReference("Usuario/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Usuario value = dataSnapshot.getValue(Usuario.class);
                        break;
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
    public void irProduto(View view){
        Intent intent = new Intent(this, ProcurarProdutos.class);
        startActivity(intent);
    }
    public void irServicos(View view){
        Intent intent = new Intent(this, ProcurarServicos.class);
        startActivity(intent);
    }
    public void irSeuPerfil(View view){
        Intent intent = new Intent(this, AtualizaUsuario.class);
        startActivity(intent);
    }
    public void irSeuFavoritado(View view){
        Intent intent = new Intent(this, VerMeusFavoritados.class);
        startActivity(intent);
    }
    public void irListaUsuario(View view){
        Intent intent = new Intent(this, ListaUsuario.class);
        startActivity(intent);
    }
}
