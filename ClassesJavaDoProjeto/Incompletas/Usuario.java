import java.util.*;

public class Usuario {
	protected String nome;                    //Tive que colocar os atributos protected para poder acess�-los na classe AdmUsuario
	protected String telefone;
	protected String curso;
	protected String login;
	protected String senha;
	private	ArrayList<Anuncio> anuncios;
	private ArrayList<Integer> avaliacoes;
	private int mediaAvaliacao;
	private int qtdAvaliacao;
	private ArrayList<Anuncio> favoritos;
	private boolean compraRealizada;
	
	public Usuario(String nome, String telefone, String curso, String login, String senha) {
		if (nome == "") {
			throw new IllegalArgumentException ("Campo vazio. Por favor, informe seu nome");
		} else {
			this.nome = nome;
		}
		if (telefone == "") {
			throw new IllegalArgumentException ("Campo vazio. Por favor, informe seu telefone");
		} else {
			this.telefone = telefone;
		}
		if (curso == "") {
			throw new IllegalArgumentException ("Campo vazio. Por favor, informe seu curso");
		} else {
			this.curso = curso;
		}
		if (login == "") {
			throw new IllegalArgumentException ("Campo vazio. Por favor, informe seu login");
		} else {
			this.login = login;
		}
		if (senha == "") {
			throw new IllegalArgumentException ("Campo vazio. Por favor, informe sua senha");
		} else {
			this.senha = senha;
		}
		this.anuncios = new ArrayList<Anuncio>();
		this.favoritos = new ArrayList<Anuncio>();
		this.avaliacoes = new ArrayList<Integer>();
	}
	
	public void criarAnuncio(String nome, String descricao, double preco) {
		Anuncio novo = new Anuncio (nome, descricao, preco);
		novo.setDisponibilidade(true);
		this.anuncios.add(novo);
	}
	
	public void alterarDisponibilidade(int posicao) {     //Alterei o par�metro do m�todo de Anuncio para posicao para pegar direto no ArrayList
		if (this.anuncios.get(posicao).getDisponibilidade()) {
			this.anuncios.get(posicao).setDisponibilidade(false);
		} else {
			this.anuncios.get(posicao).setDisponibilidade(true);
		}
	}
	
	public void finalizarAnuncio(int posicao) {
		this.anuncios.remove(posicao);
	}
	
	// polimorfismo de sobrecarga dependendo do que o usu�rio quiser alterar
	public void editarAnuncio(int posicao, String nome) {
		this.anuncios.get(posicao).setNome(nome);
	}
	
	public void editarAnuncio(String descricao, int posicao) {
		this.anuncios.get(posicao).setDescricao(descricao);
	}
	
	public void editarAnuncio(int posicao, double preco) {
		this.anuncios.get(posicao).setPreco(preco);
	}
	
	public void editarAnuncio(int posicao, String nome, String descricao) {
		this.anuncios.get(posicao).setNome(nome);
		this.anuncios.get(posicao).setDescricao(descricao);
	}
	
	public void editarAnuncio(String nome, double preco, int posicao) {
		this.anuncios.get(posicao).setNome(nome);
		this.anuncios.get(posicao).setPreco(preco);
	}
	
	public void editarAnuncio(int posicao, String descricao, double preco) {
		this.anuncios.get(posicao).setDescricao(descricao);
		this.anuncios.get(posicao).setPreco(preco);
	}
	
	//Incompleta
	public void favoritar(Anuncio anuncio) {
		anuncio.addFavorito(Usuario favoritador);
	}
	// como vai chamar o whatsapp, esse m�todo s� mostra o telefone do vendedor
	public void contatarVendedor(Anuncio anuncio) {
		AdmUsuario adm = new AdmUsuario();
		int i = adm.getUsuarios().indexOf(anuncio.getVendedor());
		System.out.println(adm.getUsuarios().get(i).telefone);
	}
	
	public void avaliarVendedor(Anuncio anuncio, int avaliacao) {
		anuncio.getVendedor().avaliacoes.add(avaliacao);
	}
	
	// teria que colocar o arrayList avaliacoes da classe Anuncio como protected para poder acess�-lo diretamente
	public void avaliarAnuncio(Anuncio anuncio, int avaliacao) {   // acrescentei a avaliacao como parametro
		anuncio.avaliacoes.add(avaliacao);
	}
	
	public void chamarPaypal() {
		
	}
	
	// N�o lembro muito bem como funciona esse m�todo,
	// mas acredito que � s� para trocar o valor da vari�vel compraRealizada (que eu n�o lembro pra que serve exatamente)
	public void compraEfetuada() {
		this.compraRealizada = true;
	}
}
