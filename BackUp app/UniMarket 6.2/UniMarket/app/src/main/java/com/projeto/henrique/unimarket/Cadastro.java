package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class Cadastro extends AppCompatActivity {
    private DatabaseReference database;
    private StorageReference Ref;
    private Usuario user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] cursos = {"Sistemas de Informação", "Medicina", "Biologia", "Direito"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cursos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
    public  void CadastrarUsuario(View view)throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
        try {
            boolean repete = false;
            EditText n = (EditText) findViewById(R.id.nome);
            String nome = n.getText().toString();
            EditText l = (EditText) findViewById(R.id.login);
            String login = l.getText().toString();
            login = retiraEspacos(login);
            EditText s = (EditText) findViewById(R.id.senha);
            String senha = s.getText().toString();
            EditText ns = (EditText) findViewById(R.id.confsenha);
            String confsenha = ns.getText().toString();
            EditText nu = (EditText) findViewById(R.id.numero);
            String numero = "5521"+nu.getText().toString();
            if(isCampoVazio(nome)){
                n.requestFocus();
                repete = true;
            }
            else if(!isEmailValido(login)){
                l.requestFocus();
                repete = true;
            }
            else if(isCampoVazio(senha)){
                s.requestFocus();
                repete = true;
            }
            else if(isCampoVazio(confsenha)){
                ns.requestFocus();
                repete = true;
            }
            else if(isCampoVazio(numero)){
                nu.requestFocus();
                repete = true;
            }
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            String curso = spinner.getSelectedItem().toString();
            if(repete){
                AlertDialog.Builder aviso = new AlertDialog.Builder(this);
                aviso.setTitle(R.string.title_aviso);
                aviso.setMessage(R.string.message_aviso);
                aviso.setNeutralButton(R.string.neutral_button, null);
                aviso.show();
            }
            else if (senha.equals(confsenha)) {
                login = login.replace("@", "A");
                login = login.replace(".", "P");
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));
                StringBuilder hexString = new StringBuilder();
                for (byte b : messageDigest) {
                    hexString.append(String.format("%02X", 0xFF & b));
                }
                senha = hexString.toString();
                user = new Usuario(nome, numero, curso, login, senha);
                user.setTemImagem(false);
                database = FirebaseDatabase.getInstance().getReference();
                database.child("Usuario").child(user.getLogin()).setValue(user);
                Toast.makeText(getApplicationContext(), "Cadastrado", Toast.LENGTH_SHORT).show();
                Intent cadastrado = new Intent(Cadastro.this, MainActivity.class);
                startActivity(cadastrado);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //verifica primeiro se o campo informado está vazio, caso falso verifica se sem espaços continua vazio
    public boolean isCampoVazio(String valor){
        boolean resposta = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resposta;

    }
    //Verifica se o email digitado foi válido.
    //Primeiro checa pela função isCampoVazio se o campo está vazio.
    //Em seguida chega o padrão digitado está nos conformes.
    public boolean isEmailValido(String valor){
        boolean valido;
        if((isCampoVazio(valor) == false) && (android.util.Patterns.EMAIL_ADDRESS.matcher(valor).matches())){
            valido = true;
        }
        else{
            valido = false;
        }
        return valido;
    }
    public void irLogin(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static String retiraEspacos(String valor){
        valor = valor.replace(" ", "");
        return valor;
    }
}
