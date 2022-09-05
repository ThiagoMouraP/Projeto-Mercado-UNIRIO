package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference database;
    private Usuario user = new Usuario();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void irCadastro(View view){
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
    }
    public void entrar(View view)throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
        final Intent intent = new Intent(this, Principal.class);
        EditText n = (EditText) findViewById(R.id.login);
        String login = n.getText().toString();
        login = login.replace("@", "A");
        login = login.replace(".", "P");
        login = retiraEspacos(login);
        EditText s = (EditText) findViewById(R.id.senha);
        String senha = s.getText().toString();
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }
        senha = hexString.toString();
        user.setLogin(login);
        user.setSenha(senha);
        database = FirebaseDatabase.getInstance().getReference("Usuario/" + user.getLogin());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario value = dataSnapshot.getValue(Usuario.class);
                try {
                    if ((value.getLogin().equals(user.getLogin()))&&(value.getSenha().equals(user.getSenha()))) {
                        Toast.makeText(getApplicationContext(), "Bem Vindo", Toast.LENGTH_SHORT).show();
                        database = FirebaseDatabase.getInstance().getReference();
                        intent.putExtra("nome",  value);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }
    //retira os espaços em branco do login
    public static String retiraEspacos(String valor){

        valor = valor.replace(" ", "");
        return valor;

    }
}
