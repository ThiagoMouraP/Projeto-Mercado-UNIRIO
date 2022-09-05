package com.projeto.henrique.unimarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class informacoesServicoDono extends AppCompatActivity {
    private DatabaseReference database;
    private Servico venda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_servico_dono);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setIsIndicator(true);
        Intent intent = getIntent();
        final Servico servico = (Servico) intent.getSerializableExtra("nome");
        ratingBar.setRating(servico.getAvaliacao());
        EditText n = (EditText) findViewById(R.id.nome);
        n.setText(servico.getNome());
        EditText l = (EditText) findViewById(R.id.descricao);
        l.setText(servico.getDescricao());
        EditText s = (EditText) findViewById(R.id.tag1);
        s.setText(servico.getTag().get(0));
        EditText ns = (EditText) findViewById(R.id.tag2);
        ns.setText(servico.getTag().get(1));
        EditText nu = (EditText) findViewById(R.id.preco);
        nu.setText(""+servico.getPreco());
        EditText hour = (EditText) findViewById(R.id.horario);
        hour.setText(servico.getHorario());
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] opcao = {"Disponível", "Indisponível"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcao);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        if(servico.isDisponibilidade()){
            spinner.setSelection(0);
        }
        else{
            spinner.setSelection(1);
        }
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        String[] opcao2 = {"Presencial", "A distancia"};
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcao2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);
        if(servico.isPresencial()){
            spinner2.setSelection(0);
        }
        else{
            spinner2.setSelection(1);
        }
    }
    public void Salvar(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        LinearLayout linha1 = findViewById(R.id.linha1);
        LinearLayout linha2 = findViewById(R.id.linha2);
        EditText n = (EditText) findViewById(R.id.nome);
        String nome = n.getText().toString();
        EditText l = (EditText) findViewById(R.id.descricao);
        String descricao = l.getText().toString();
        EditText s = (EditText) findViewById(R.id.tag1);
        String tag1 = s.getText().toString();
        EditText ns = (EditText) findViewById(R.id.tag2);
        String tag2 = ns.getText().toString();
        EditText nu = (EditText) findViewById(R.id.preco);
        String numero = nu.getText().toString();
        double preco = Double.parseDouble(numero);
        ArrayList<String> tag = new ArrayList<>();
        tag.add(tag1);
        tag.add(tag2);
        tag.add(nome);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String disponibilidade = spinner.getSelectedItem().toString();
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        String tipo = spinner2.getSelectedItem().toString();
        venda = new Servico(Principal.retornaUsuario(), preco, nome, descricao, tag, informacoesProdutoDono.retornarValor(disponibilidade));
        venda.setPresencial(retornaPresencial(tipo));
        if(venda.isPresencial()){
            linha1.setVisibility(View.GONE);
            linha2.setVisibility(View.VISIBLE);
        }
        else{
            entrarNoBanco();
        }

    }
    public static boolean retornaPresencial(String tipo) {
        if(tipo.equals("Presencial")){
            return true;
        }
        else{
            return false;
        }
    }
    public void colocarServicoNoBanco() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Servico").child(AdicionarVenda.gerarId(venda.getNome(), Principal.retornaUsuario().getLogin())).setValue(venda);
        Toast.makeText(getApplicationContext(), "Salvo", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Principal.class);
        intent.putExtra("nome", Principal.retornaUsuario());
        startActivity(intent);
    }
    public void SalvarServico(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        EditText l = (EditText) findViewById(R.id.horario);
        String horario = l.getText().toString();
        venda.setHorario(horario);
        entrarNoBanco();
    }
    public void Excluir(View view){
        Intent intent2 = getIntent();
        final Anuncio produto = (Servico) intent2.getSerializableExtra("nome");
        final Intent intent = new Intent(this, Principal.class);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Você tem certeza que quer excluir este anuncio?");
        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            produto.remover();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Excluído", Toast.LENGTH_SHORT).show();
                        intent.putExtra("nome",  Principal.retornaUsuario());
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void verFavoritado(View view){
        Intent intent2 = getIntent();
        final Anuncio produto = (Servico) intent2.getSerializableExtra("nome");
        Intent intent = new Intent(this, Favoritado.class);
        intent.putExtra("nome",  produto);
        startActivity(intent);
    }
    public void entrarNoBanco() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Intent intent = getIntent();
        final Servico servico = (Servico) intent.getSerializableExtra("nome");
        if(!(venda.getNome().equals(servico.getNome()))) {
            servico.remover();
        }
        colocarServicoNoBanco();
    }


}
