package com.projeto.henrique.unimarket;



import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by henrique on 18/05/2018.
 */

public class Produto extends Anuncio {
    private int quantidade;
    public Produto(){

    }
    public Produto(Usuario u, double p, String n, String d, ArrayList<String> t, boolean disponibilidade){
        super(u, p, n, d, t, disponibilidade);
    }
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public String toString(){
        return "Nome do Produto: "+this.getNome()+"\n"+
                "Descricao: "+this.getDescricao()+"\n"+
                "Preço: "+this.getPreco()+"\n"+
                "Quantidade: "+this.getQuantidade()+"\n"+
                "Anunciante: "+this.getVendedor().getNome();

    }
    public void remover() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        database = FirebaseDatabase.getInstance().getReference();
        database.child(AdicionarVenda.gerarId(this.getNome(),this.getVendedor().getLogin())).removeValue();
    }
    public void favoritar(Usuario user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(verificarUsuarioFavoritado(user)){
            this.getFavoritado().add(user);
            database = FirebaseDatabase.getInstance().getReference();
            database.child("Produto").child(AdicionarVenda.gerarId(this.getNome(),this.getVendedor().getLogin())).child("favoritado").setValue(this.getFavoritado());
        }
    }
    public void avaliar(float rating) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Produto").child(AdicionarVenda.gerarId(this.getNome(),this.getVendedor().getLogin())).child("qtdAvalicao").setValue(this.getQtdAvalicao()+1);
        database.child("Produto").child(AdicionarVenda.gerarId(this.getNome(),this.getVendedor().getLogin())).child("avaliacao")
                .setValue((rating+this.getAvaliacao())/(this.getQtdAvalicao()));

    }

}
