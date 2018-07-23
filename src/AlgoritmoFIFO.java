import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlgoritmoFIFO {
	HashMap<Integer, Integer> frame = new HashMap<>();
	List<Pagina> listPaginas = new ArrayList<>();
	
	private int tamPagina = 0;
	private int contPF = 0;
	private int modificado = 0;
	private int numFrames = 0;
	private String caminhoArquivo;
	
	public AlgoritmoFIFO() {
		
	}
	
	public AlgoritmoFIFO(String caminhoArquivo, int numFrames, int tamPagina) {
		this.numFrames = numFrames;
		this.caminhoArquivo = caminhoArquivo;
		this.tamPagina = tamPagina;
	}
	
	public void gerenciadorFIFO() {
		inicializaList();
		lerArquivo();
		System.out.println("-----------------------------------------------");
		System.out.println("* Execução terminada ...");
		System.out.println("-----------------------------------------------");
		System.out.println("Dados da ultima execução:");
		System.out.println("Algoritmo utilizado: FIFO");
		System.out.println("Número de Page Faults: " + contPF);
		System.out.println("Número de writes: " + modificado);
		System.out.println("Parametros utilizados durante esta execução:");
		System.out.println("Numero de Frames: " + numFrames + " Tamanho da página: " + tamPagina + " Arquivo: " + caminhoArquivo);
		System.out.print("-----------------------------------------------");
	}
	
	public void lerArquivo() {
		BufferedReader br;
		try {
			FileReader ler = new FileReader(caminhoArquivo);
			BufferedReader reader = new BufferedReader(ler);
			String linha;
			
			int instrucao;
			String tipo;
			int dado;

			while (!(linha = reader.readLine()).equals("#eof")) {
				linha = linha.replace(":", "");
				String[] texto = linha.split(" ");
				instrucao = Integer.parseInt(texto[0].substring(2, 9), 16)/tamPagina;
				tipo = texto[1].toLowerCase();
				dado = Integer.parseInt(texto[2].substring(2, 9), 16)/tamPagina;
				
				verificaHash(instrucao, "r");
				verificaHash(dado, tipo);
				//System.out.println(instrucao + " " + tipo + " " + dado);
			}

		} catch (IOException e) {
			System.err.println("Arquivo de dados não encontrado, verifique o caminho ...");
			e.printStackTrace();
		}
	}
	
	public void inicializaList() {
		for(int i = 0; i < numFrames; i ++) {
			Pagina nova = new Pagina();
			listPaginas.add(nova);
		}
	}
	
	public void verificaHash(int posicao, String tipo) {
		if(frame.get(posicao) == null) {
			frame.put(posicao, -1);
			// Caso não exista no HASH é criado
		}
		
		if(frame.get(posicao) == -1) {
			pageFaultFIFO(posicao, tipo);
			contPF++;
		}
		else {
			refreshPage(posicao, tipo);
		}
	}
	
	public void pageFaultFIFO(int posicao, String tipo) {
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(pagina.getNumPag() == -1) {
				pagina.setNumPag(posicao);
				pagina.setR(true);
				if(tipo.equals("w")) {
					pagina.setW(true);
				}
				frame.replace(posicao, i);
				return;
			}
		}
		
		frame.replace(listPaginas.get(0).getNumPag(), -1);
		if(listPaginas.get(0).isW()) {
			modificado++;
		}
		listPaginas.remove(0);
		
		Pagina pagina = new Pagina();
		pagina.setNumPag(posicao);
		pagina.setR(true);
		pagina.setW(false);
		if(tipo.equals("w")) {
			pagina.setW(true);
		}
		frame.replace(posicao, listPaginas.size());
		listPaginas.add(pagina);
	}
	
	public void refreshPage(int posicao, String tipo) {
		int id = frame.get(posicao);
		listPaginas.get(id).setR(true);
		if(tipo.equals("w")) {
			listPaginas.get(id).setW(true);
		}
	}	
}
