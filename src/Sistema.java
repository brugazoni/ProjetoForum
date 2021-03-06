import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Sistema {
	public ArrayList<Mensagem> postagens;
	public ArrayList<Usuario> usuarios;
	public ArrayList<UsuarioModerador> moderadores;
	public HashMap<String, String> mensagensAutomaticas;
	
	private ArrayList<Mensagem> pendentes;
	/////////////////////////////////////////////////
	
	public Sistema(){
		this.postagens = new ArrayList<Mensagem>();
		this.usuarios = new ArrayList<Usuario>();
		this.moderadores = new ArrayList<UsuarioModerador>();
		this.pendentes = new ArrayList<Mensagem>();
		this.mensagensAutomaticas = new HashMap<String, String>();
	}

	public HashMap<String, String> getMensagensAutomaticas() {
		return mensagensAutomaticas;
	}

	public void setMensagensAutomaticas(HashMap<String, String> mensagensAutomaticas) {
		this.mensagensAutomaticas = mensagensAutomaticas;
	}

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public ArrayList<Mensagem> getPostagens() {
		return postagens;
	}

	public void setPostagens(ArrayList<Mensagem> postagens) {
		this.postagens = postagens;
	}

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void aprovaMensagem(Mensagem m){
		this.postagens.add(m);
		this.pendentes.remove(m);
	}
	
	public void reprovaMensagem(Mensagem m){
		this.pendentes.remove(m);
	}
	
	public ArrayList<UsuarioModerador> getModeradores() {
		return moderadores;
	}

	public void setModeradores(ArrayList<UsuarioModerador> moderadores) {
		this.moderadores = moderadores;
	}

	public ArrayList<Mensagem> getPendentes() {
		return pendentes;
	}

	public void setPendentes(ArrayList<Mensagem> pendentes) {
		this.pendentes = pendentes;
	}

	public void imprimePostagens(){
		System.out.println("\t//////////////////////////////");
		for(Mensagem i : this.postagens){
			System.out.println(i.getAutor() + ":");
			System.out.println("\t" + i.getTitulo() + ":");
			System.out.println("\t" + i.getCorpo_texto());
			if (i instanceof PostagemPublica) System.out.println("\t+" + ((PostagemPublica) i).getReacoes());
			
			System.out.println("\n");
			for(Comentario j : i.getComentarios()){
				System.out.println("\t" + j.getAutor() + ":");
				System.out.println("\t\t" + j.getCorpo_texto());
			}
		}
		System.out.println("\t//////////////////////////////");
	}
	
	public Usuario estaNoSistema(String nome, String id){
		for(Usuario i : this.getUsuarios()){
			if(i.getNome().equals(nome)){
				if(i.getId().equals(id)) return i;
			}
		}
		return null;
	}
	
	public void leUsuarios() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("src/usuarios.csv"));
		String buffer;
		String[] campos = new String[3];
		while((buffer = reader.readLine())!=null && buffer.length() > 2){
			campos = buffer.split(";");
			new Usuario(this, campos[0], campos[1]);
			if (campos.length > 2){
				new UsuarioModerador(this, campos[0], campos[1], campos[2]);
			}
		}
		reader.close();
	}
	
	public void imprimeUsuarios(){
		System.out.println("Usuarios comuns:");
		for(Usuario u : this.usuarios){
			System.out.println(u.getNome() + ", e senha: " + u.getId());
		}
		System.out.println("Usuarios moderadores:");
		for(UsuarioModerador um : this.moderadores){
			System.out.println(um.getNome() + ", senha: " + um.getId() + ", cargo: " + um.getCargo());
		}
	}
	
	public void criaMensagensAutomaticas() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("src/tags.csv"));
		String buffer;
		String[] campos = new String[2];
		while((buffer = reader.readLine())!=null && buffer.length() > 2){
			campos = buffer.split(";");
			this.mensagensAutomaticas.put(campos[0], campos[1]);
		}
		reader.close();
	}
	
	public void imprimeMensagensAutomaticas(){
		for(Entry<String, String> i : this.mensagensAutomaticas.entrySet()){
			System.out.println(i.getKey() + ", e resposta e: " + i.getValue());
		}
	}
	
	public void criaPostagensArquivo() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("src/postagens.csv"));
		String buffer;
		String[] campos = new String[3];
		while((buffer = reader.readLine())!=null && buffer.length() > 2){
			campos = buffer.split(";");
			PostagemPublica p = new PostagemPublica(this);
			p.setAutor(campos[0]);
			p.setTitulo(campos[1]);
			p.setCorpo_texto(campos[2]);
			
			this.postagens.add(p);
		}
		reader.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
