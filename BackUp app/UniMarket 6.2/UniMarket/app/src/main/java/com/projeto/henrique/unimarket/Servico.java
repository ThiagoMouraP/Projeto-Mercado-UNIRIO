package com.projeto.henrique.unimarket;

import android.graphics.Bitmap;

import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by henrique on 18/05/2018.
 */

public class Servico extends Anuncio {
    private String horario;
    private boolean presencial;
    public Servico(){

    }
    public Servico(Usuario u, double p, String n, String d, ArrayList<String> t, boolean disponibilidade){
        super(u, p, n, d, t, disponibilidade);
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public boolean isPresencial() {
        return presencial;
    }

    public void setPresencial(boolean presencial) {
        this.presencial = presencial;
    }
    public String toString(){
        return "Nome do Produto: "+this.getNome()+"\n"+
                "Descricao: "+this.getDescricao()+"\n"+
                "Preço: "+this.getPreco()+"\n"+
                presencial()+"\n"+
                "Anunciante: "+this.getVendedor().getNome();
    }
    public void remover() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Servico").child(AdicionarVenda.gerarId(this.getNome(),this.getVendedor().getLogin())).removeValue();
    }
    private String presencial(){
        if(presencial){
            return "Presencial \n Horário: "+horario;
        }
        else{
            return "A distancia";
        }
    }
    public void favoritar(Usuario user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(verificarUsuarioFavoritado(user)) {
            this.getFavoritado().add(user);
            database = FirebaseDatabase.getInstance().getReference();
            database.child("Servico").child(AdicionarVenda.gerarId(this.getNome(),this.getVendedor().getLogin())).child("favoritado").setValue(this.getFavoritado());
        }
    }
    public void avaliar(float rating) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Servico").child(AdicionarVenda.gerarId(this.getNome(),this.getVendedor().getLogin())).child("qtdAvalicao").setValue(this.getQtdAvalicao()+1);
        database.child("Servico").child(AdicionarVenda.gerarId(this.getNome(),this.getVendedor().getLogin())).child("avaliacao")
                .setValue((rating+this.getAvaliacao())/(this.getQtdAvalicao()));

    }
}
