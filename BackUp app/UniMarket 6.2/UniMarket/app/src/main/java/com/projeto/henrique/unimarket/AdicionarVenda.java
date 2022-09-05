package com.projeto.henrique.unimarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class AdicionarVenda extends AppCompatActivity {
    private DatabaseReference database;
    private Anuncio anuncio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_venda);
        String[] tipo = {"Produto", "Serviço"};
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        String[] tipo2 = {"Presencial", "A distância"};
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);

        String[] tag1 = {"Comida", "Livro"};
        final Spinner spinerTag1 = (Spinner) findViewById(R.id.TAG1);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,tag1);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerTag1.setAdapter(dataAdapter3);

        final Spinner spinerTag2 = (Spinner) findViewById(R.id.TAG2);
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,tag1);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerTag2.setAdapter(dataAdapter4);

    }
    public void CadastrarAnuncio(View view) {
        //Layout inicial da tela (não oculto)
        LinearLayout linha = findViewById(R.id.linha);
        //Layout da escolhe um produto (oculto)
        LinearLayout linha2 = findViewById(R.id.linha2);
        //Layout da escolhe um serviço (oculto)
        LinearLayout linha3 = findViewById(R.id.linha3);
        EditText n = (EditText) findViewById(R.id.nome);
        String nome = n.getText().toString();
        //alterar login para descrição anuncio
        EditText l = (EditText) findViewById(R.id.login);
        String login = l.getText().toString();
        Spinner spinerTag1 = (Spinner) findViewById(R.id.TAG1);
        String tag1 = spinerTag1.getSelectedItem().toString();
        Spinner spinerTag2 = (Spinner) findViewById(R.id.TAG2);
        String tag2 = spinerTag2.getSelectedItem().toString();
        EditText nu = (EditText) findViewById(R.id.numero);
        String numero = nu.getText().toString();
        //Confere se os campos estão vazios. Caso vazio aparece o aviso para preenchelos
        if(!isCampoVazio(nome)||!isCampoVazio(login)||!isCampoVazio(tag1)||!isCampoVazio(tag2)||!isCampoVazio(numero)) {
            double preco = Double.parseDouble(numero);
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            String curso = spinner.getSelectedItem().toString();
            ArrayList<String> tag = new ArrayList<>();
            tag.add(tag1);
            tag.add(tag2);
            tag.add(nome);
            linha.setVisibility(View.GONE);
            if (curso.equals("Produto")) {
                Anuncio venda = new Produto(Principal.retornaUsuario(), preco, nome, login, tag, true);
                anuncio = (Produto) venda;
                linha2.setVisibility(View.VISIBLE);
            } else {
                Anuncio venda = new Servico(Principal.retornaUsuario(), preco, nome, login, tag, true);
                anuncio = (Servico) venda;
                linha3.setVisibility(View.VISIBLE);
            }
        }
        else{
            mostrarDialogo();
        }
    }
    public void CadastrarProduto(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        EditText qtd = (EditText) findViewById(R.id.quantidade);
        String quant = qtd.getText().toString();
        if(!isCampoVazio(quant)) {
            int quantidade = Integer.parseInt(quant);
            ((Produto)anuncio).setQuantidade(quantidade);
            database = FirebaseDatabase.getInstance().getReference();
            database.child("Produto").child(gerarId(((Produto)anuncio).getNome(), Principal.retornaUsuario().getLogin())).setValue(((Produto)anuncio));
            Toast.makeText(getApplicationContext(), "Cadastrado", Toast.LENGTH_SHORT).show();
            voltar();
        }
        else{
            mostrarDialogo();
        }
    }
    public void CadastrarServico(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        LinearLayout linha4 = findViewById(R.id.linha4);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ((Servico)anuncio).setPresencial(retornarPresencial(spinner2.getSelectedItem().toString()));
        if(((Servico)anuncio).isPresencial()){
            linha4.setVisibility(View.VISIBLE);
        }
        else{
            ((Servico)anuncio).setHorario(" ");
            cadastrarServicoBanco();
        }

    }
    public static boolean retornarPresencial(String presencial){
        if(presencial.equals("Presencial")){
            return true;
        }
        else{
            return false;
        }
    }
    public void CadastrarHorario(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        EditText hour = (EditText) findViewById(R.id.horario);
        String horario = hour.getText().toString();
        if(!isCampoVazio(horario)) {
            ((Servico)anuncio).setHorario(horario);
            cadastrarServicoBanco();
        }
        else{
            mostrarDialogo();
        }

    }
    public void cadastrarServicoBanco() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Servico").child(gerarId(((Servico)anuncio).getNome(), Principal.retornaUsuario().getLogin())).setValue(((Servico)anuncio));
        Toast.makeText(getApplicationContext(), "Cadastrado", Toast.LENGTH_SHORT).show();
        voltar();
    }

    public static String gerarId(String a1, String a2) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        byte messageDigest[] = algorithm.digest((a1+a2).getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }
         return hexString.toString();
    }
    public void voltar(){
        Intent intent = new Intent(this, Principal.class);
        intent.putExtra("nome", Principal.retornaUsuario());
        startActivity(intent);
    }
    public boolean isCampoVazio(String valor) {
        boolean resposta = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resposta;
    }
    public void mostrarDialogo(){
        AlertDialog.Builder aviso = new AlertDialog.Builder(this);
        aviso.setTitle(R.string.title_aviso);
        aviso.setMessage(R.string.message_aviso);
        aviso.setNeutralButton(R.string.neutral_button, null);
        aviso.show();
    }
    public void ChooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                anuncio.setImagem(Base64.encodeToString(byteArray, Base64.DEFAULT));
                anuncio.setTemImagem(true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
