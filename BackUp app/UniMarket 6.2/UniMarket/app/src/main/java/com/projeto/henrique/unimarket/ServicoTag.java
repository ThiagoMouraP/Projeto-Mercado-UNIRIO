package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServicoTag extends AppCompatActivity {
    ArrayList<String> nomes = new ArrayList<>();
    ArrayList<Anuncio> anuncios = new ArrayList<>();
    private DatabaseReference database;
    private ArrayList<Drawable> imagem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico_tag);
        EditText busca = (EditText) findViewById(R.id.busca);
        busca.setHintTextColor(Color.WHITE);
        busca.setTextColor(Color.WHITE);
        final Intent intent = new Intent(this, Principal.class);
        final Intent intent2 = new Intent(this, informacoesServico.class);
        Intent intent3 = getIntent();
        final String servico= (String) intent3.getSerializableExtra("nome");
        consultarServicos(servico);
        ListView lista = (ListView) findViewById(R.id.line1);
        CustomListAdapter adapter=new CustomListAdapter(this, nomes, imagem);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    intent.putExtra("nome", Principal.retornaUsuario());
                    startActivity(intent);
                }
                else{
                    //A imagem ?? retirada do objeto para que n??o haja bugs quando este por passado
                    //de uma tela para a outra
                    anuncios.get(position-1).setImagem("");
                    intent2.putExtra("nome", anuncios.get(position-1));
                    startActivity(intent2);
                }
            }
        });
    }
    public void consultarServicos(final String tag) {
        database = FirebaseDatabase.getInstance().getReference("Servico/");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Servico value = dataSnapshot.getValue(Servico.class);
                        if (!(value.getVendedor().getNome().equals(Principal.retornaUsuario().getNome()))) {
                            if (value.isDisponibilidade()) {
                                if(value.getTag().contains(tag)) {
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
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    public void Procurar(View view){
        EditText busca = (EditText) findViewById(R.id.busca);
        String tag = busca.getText().toString();
        Intent intent = new Intent(this, ServicoTag.class);
        intent.putExtra("nome",  tag);
        startActivity(intent);
    }
}
