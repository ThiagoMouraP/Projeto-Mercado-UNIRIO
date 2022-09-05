import java.util.*;

public class AdmUsuario {
	private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
	
	public void setNome(String nome, int posicao) {
		this.usuarios.get(posicao).nome = nome;
	}
	
	public void setTelefone(String telefone, int posicao) {
		this.usuarios.get(posicao).telefone = telefone;
	}

	public void setCurso(String curso, int posicao) {
		this.usuarios.get(posicao).curso = curso;
	}
	
	public void setLogin(String login, int posicao) {
		this.usuarios.get(posicao).login = login;
	}

	public void setSenha(String senha, int posicao) {
		this.usuarios.get(posicao).senha = senha;
	}

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}
	
}
