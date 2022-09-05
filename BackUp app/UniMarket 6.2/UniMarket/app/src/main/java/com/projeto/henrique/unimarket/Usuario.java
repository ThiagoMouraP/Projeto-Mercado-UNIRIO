package com.projeto.henrique.unimarket;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by henrique on 18/05/2018.
 */

public class Usuario implements Serializable, Avaliavel {
    private DatabaseReference database;
    private DatabaseReference database2;
    private String nome;
    private String telefone;
    private String curso;
    private String login;
    private String senha;
    private float mediaAvaliacao;
    private int qtdAvaliacao;
    private ArrayList<Anuncio> anuncio;
    private ArrayList<Anuncio> anuncioFavoritado;
    private boolean temImagem;
    private String imagem;
    public Usuario(){
        anuncio = new ArrayList<Anuncio>();
        anuncioFavoritado = new ArrayList<Anuncio>();
        qtdAvaliacao = 0;
    }
    public Usuario(String n, String t, String c, String l, String s){
        anuncio = new ArrayList<Anuncio>();
        anuncioFavoritado = new ArrayList<Anuncio>();
        qtdAvaliacao = 0;
        nome = n;
        telefone = t;
        curso = c;
        login = l;
        senha = s;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public boolean isTemImagem() {
        return temImagem;
    }

    public void setTemImagem(boolean temImagem) {
        this.temImagem = temImagem;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCurso() {
        return curso;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public float getMediaAvaliacao() {
        return mediaAvaliacao;
    }

    public int getQtdAvaliacao() {
        return qtdAvaliacao;
    }

    public ArrayList<Anuncio> getAnuncio() {
        return anuncio;
    }

    public ArrayList<Anuncio> getAnuncioFavoritado() {
        return anuncioFavoritado;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setAnuncio(ArrayList<Anuncio> anuncio) {
        this.anuncio = anuncio;
    }

    public String toString(){
        return "Nome :"+nome+"\n"+
                "Curso :"+curso+"\n"+
                "Número :"+telefone+"\n"+
                "E-mail :"+login.replace("A", "@").replace("P", ".");
    }
    public boolean equals(Object object){
        Usuario user = (Usuario) object;
        if (this.getLogin().equals(user.getLogin())) {
            return true;
        } else
            {
            return false;
        }
    }
    public void avaliar(float rating) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Usuario").child(this.getLogin()).child("qtdAvaliacao").setValue(this.getQtdAvaliacao()+1);
        database.child("Usuario").child(this.getLogin()).child("mediaAvaliacao")
                .setValue((rating+this.getMediaAvaliacao())/(this.getQtdAvaliacao()));
    }
}
