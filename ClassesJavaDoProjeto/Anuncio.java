import java.util.ArrayList;
public class Anuncio implements Produtos,Servico {
		private ArrayList<Usuario> listaFavoritaram = new ArrayList<>();
		private Usuario vendedor;
		private boolean disponibilidade = true;
		private double preco;
		private String nome;
		private String descricao;
		private ArrayList<String> tags = new ArrayList<>();
		private ArrayList<Integer> avaliacoes = new ArrayList<>();
		private float avaliMedia;
		private int quantidade = 1; //Comecei com um porque parece o mais lógico e está aqui pq não deveria ser estático em Produtos.
		private boolean presencial; //O mesmo para presencial e horario, só que em Servico
		private String horario;
		
		//Para caso seja um produto
		Anuncio(Usario vendedor, double preco, String nome, String descricao){
			this.vendedor = vendedor;
			this.preco = preco;
			this.nome = nome;
			this.descricao = descricao;
		}
		
		//Para caso seja um servico
		Anuncio(Usario vendedor, double preco, String nome, String descricao, boolean presencial, String horario){
			this.vendedor = vendedor;
			this.preco = preco;
			this.nome = nome;
			this.descricao = descricao;
			this.presencial = presencial;
			this.horario = horario;
		}
		public void setQuantidade(int quantidade) {
			this.quantidade = quantidade;
		}
		public int getQuantidade() {
			return this.quantidade;
		}
		public float getMedia() {
			return this.avaliMedia;
		}
		public void setPresencial(boolean newPresenc) {
			this.presencial = newPresenc;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		public void setDisponibilidade(boolean disp) {
			this.disponibilidade = disp;
		}
		public void setPreco(double preco) {
			this.preco = preco;
		}
		public void addFavorito(Usuario favoritador) {
			listaFavoritaram.add(favoritador);
		}
		public void calculaMedia() {
			int soma = 0;
			for(int avali: avaliacoes) {
				soma+= avali;
			}
			this.avaliMedia = soma/avaliacoes.size();
		}
}
