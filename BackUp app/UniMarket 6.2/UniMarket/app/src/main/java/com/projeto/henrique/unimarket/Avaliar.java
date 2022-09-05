package com.projeto.henrique.unimarket;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Avaliar extends AppCompatActivity {
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliar);
        Intent intent = getIntent();
        final Avaliavel anuncio = (Avaliavel) intent.getSerializableExtra("nome");
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

    }
    public void darAvaliacao(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            Intent intent = getIntent();
            final Avaliavel anuncio = (Avaliavel) intent.getSerializableExtra("nome");
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            anuncio.avaliar(ratingBar.getRating());
            Toast.makeText(getApplicationContext(), "Avaliado", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(this, Principal.class);
            intent1.putExtra("nome", Principal.retornaUsuario());
            startActivity(intent1);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

}
