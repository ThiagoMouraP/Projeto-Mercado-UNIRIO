package com.projeto.henrique.unimarket;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Base64;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by henrique on 18/05/2018.
 */

public abstract class Anuncio implements Serializable, Avaliavel {
    protected DatabaseReference database;
    private ArrayList<Usuario> favoritado;
    private Usuario vendedor;
    private boolean disponibilidade;
    private double preco;
    private String nome;
    private String descricao;
    private float avaliacao;
    private int qtdAvalicao;
    private ArrayList<String> tag;
    private String imagem;
    private boolean temImagem;
    public  Anuncio(){
        favoritado = new ArrayList<>();
    }

    public Anuncio(Usuario u, double p, String n, String d, ArrayList<String> t, boolean disponibilidade){
        temImagem = false;
        qtdAvalicao = 1;
        avaliacao = 0;
        favoritado = new ArrayList<>();
        vendedor = u;
        setDisponibilidade(disponibilidade);
        preco = p;
        nome = n;
        descricao = d;
        tag = t;
    }

    public boolean isTemImagem() {
        return temImagem;
    }

    public void setTemImagem(boolean temImagem) {
        this.temImagem = temImagem;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public ArrayList<Usuario> getFavoritado() {
        return favoritado;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public double getPreco() {
        return preco;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public ArrayList<String> getTag() {
        return tag;
    }
    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getQtdAvalicao() {
        return qtdAvalicao;
    }
    protected boolean verificarUsuarioFavoritado(Usuario user){
        for(int i = 0; i < this.getFavoritado().size(); i ++){
            if(this.getFavoritado().get(i).getNome().equals(user.getNome())){
                return false;
            }
        }
        return true;
    }

    public void setQtdAvalicao(int qtdAvalicao) {
        this.qtdAvalicao = qtdAvalicao;
    }

    public abstract void favoritar(Usuario user) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    public abstract String toString();

    public abstract void remover() throws UnsupportedEncodingException, NoSuchAlgorithmException;


}
