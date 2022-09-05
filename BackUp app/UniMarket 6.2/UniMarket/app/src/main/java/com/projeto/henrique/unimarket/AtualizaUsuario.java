package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class AtualizaUsuario extends AppCompatActivity {
    private DatabaseReference database;
    private Usuario user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualiza_usuario);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setIsIndicator(true);
        ratingBar.setRating(Principal.retornaUsuario().getMediaAvaliacao());
        String[] cursos = {"Sistemas de Informação", "Medicina", "Biologia", "Direito"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cursos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        EditText l = (EditText) findViewById(R.id.login);
        l.setText(Principal.retornaUsuario().getLogin().replace("A", "@").replace("P","." ));
        EditText nu = (EditText) findViewById(R.id.numero);
        nu.setText(Principal.retornaUsuario().getTelefone());
        spinner.setSelection(posicao(cursos));
    }
    public  void Salvar(View view)throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
        try {
            EditText l = (EditText) findViewById(R.id.login);
            String login = l.getText().toString();
            login = login.replace("@", "A");
            login = login.replace(".", "P");
            EditText s = (EditText) findViewById(R.id.senha);
            String senha = s.getText().toString();
            EditText ns = (EditText) findViewById(R.id.confsenha);
            String confsenha = ns.getText().toString();
            EditText nu = (EditText) findViewById(R.id.numero);
            String numero = nu.getText().toString();
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            String curso = spinner.getSelectedItem().toString();
            user = new Usuario();
            if(!(senha.equals(""))) {
                if (senha.equals(confsenha)) {
                    MessageDigest algorithm = MessageDigest.getInstance("MD5");
                    byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : messageDigest) {
                        hexString.append(String.format("%02X", 0xFF & b));
                    }
                    senha = hexString.toString();
                    user.setSenha(senha);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Senhas diferentes", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                user.setSenha(Principal.retornaUsuario().getSenha());
            }
            if(!(numero.equals(Principal.retornaUsuario().getTelefone()))){
                numero = "5521"+numero;
                user.setTelefone(numero);
            }
            else{
                user.setTelefone(Principal.retornaUsuario().getTelefone());
            }
            user.setCurso(spinner.getSelectedItem().toString());
            user.setNome(Principal.retornaUsuario().getNome());
            user.setAnuncio(Principal.retornaUsuario().getAnuncio());
            if(!(login.equals(Principal.retornaUsuario().getLogin()))){
                database.child("Usuario").child(Principal.retornaUsuario().getLogin()).removeValue();
                user.setLogin(login.replace("@", "A").replace(".","P" ));
            }
            else{
                user.setLogin(Principal.retornaUsuario().getLogin());
            }
            database = FirebaseDatabase.getInstance().getReference();
            database.child("Usuario").child(user.getLogin()).setValue(user);
            Toast.makeText(getApplicationContext(), "Cadastrado", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public static int posicao(String[] curso){
        for(int i = 0; i<curso.length; i++){
            if(curso[i].equals(Principal.retornaUsuario().getCurso())){
                return  i;
            }
        }
        return 0;
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
                user.setTemImagem(true);
                user.setImagem(Base64.encodeToString(byteArray, Base64.DEFAULT));

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}
