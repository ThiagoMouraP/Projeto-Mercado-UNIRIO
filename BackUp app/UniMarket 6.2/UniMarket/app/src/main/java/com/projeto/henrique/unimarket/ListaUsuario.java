package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;



public class ListaUsuario extends AppCompatActivity {
    private DatabaseReference database;
    private ArrayList<Usuario> user = new ArrayList<>();
    private ArrayList<String> lista1 = new ArrayList<>();
    private ArrayList<Drawable> imagem = new ArrayList<>();
    private StorageReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);
        final Intent intent = new Intent(this, Principal.class);
        final Intent intent2 = new Intent(this, informacaoUsuario.class);
        lista1.add("Voltar");
        imagem.add(getResources().getDrawable(R.drawable.esquerda));
        consultarUsuario();
        Collections.sort(lista1, String.CASE_INSENSITIVE_ORDER);
        ListView lista = (ListView) findViewById(R.id.line1);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista1);
        CustomListAdapter adapter=new CustomListAdapter(this, lista1, imagem);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    intent.putExtra("nome", Principal.retornaUsuario());
                    startActivity(intent);
                }
                else{
                    intent2.putExtra("nome", user.get(position-1));
                    startActivity(intent2);
                }
            }
        });
    }
    public void consultarUsuario(){
        database = FirebaseDatabase.getInstance().getReference("Usuario/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Usuario value = dataSnapshot.getValue(Usuario.class);
                        if(!(value.getLogin().equals(Principal.retornaUsuario().getLogin()))){
                            user.add(value);
                            lista1.add(value.toString());
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


}
