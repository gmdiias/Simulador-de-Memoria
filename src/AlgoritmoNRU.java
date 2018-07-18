import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlgoritmoNRU {
	HashMap<Integer, Integer> frame = new HashMap<>();
	List<Pagina> listPaginas = new ArrayList<>();
	
	private String caminhoArquivo;
	private int tamPagina = 0;
	private int contPF = 0;
	private int modificado = 0;
	private int numFrames = 0;
	private int clockTick = 0;
	
	public AlgoritmoNRU() {
		
	}
	
	public AlgoritmoNRU(String caminhoArquivo, int numFrames, int tamPagina) {
		this.numFrames = numFrames;
		this.caminhoArquivo = caminhoArquivo;
		this.tamPagina = tamPagina;
	}
	
	public void gerenciadorNRU() {
		inicializaList();
		lerArquivo();
		System.out.println("Numero de Page Faults: " + contPF);
		System.out.println("Numero de writes: " +modificado);
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
		
		clockTick++;
		verificaClock();
		if(frame.get(posicao) == -1) {
			pageFaultNRU(posicao, tipo);
			contPF++;
		}
		else {
			refreshPage(posicao, tipo);
		}
	}
	
	public void pageFaultNRU(int posicao, String tipo) {
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
		
		int altera = verificaClasses();
		
		Pagina pagina = listPaginas.get(altera);
		frame.replace(pagina.getNumPag(), -1);
		if(pagina.isW()) {
			modificado++;
		}
		pagina.setNumPag(posicao);
		pagina.setR(true);
		pagina.setW(false);
		if(tipo.equals("w")) {
			pagina.setW(true);
		}
		frame.replace(posicao, altera);
	}
	
	public int verificaClasses() {
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(!pagina.isR() && !pagina.isW()) {
				return i;
			}
		}
		
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(!pagina.isR() && pagina.isW()) {
				return i;
			}
		}
		
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(pagina.isR() && !pagina.isW()) {
				return i;
			}
		}
		
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(pagina.isR() && pagina.isW()) {
				return i;
			}
		}
		return 1;
	}
	
	public void refreshPage(int posicao, String tipo) {
		int id = frame.get(posicao);
		listPaginas.get(id).setR(true);
		if(tipo.equals("w")) {
			listPaginas.get(id).setW(true);
		}
	}	
	
	public void verificaClock() {
		if(clockTick > (numFrames*2)) {
			for(int i = 0; i < numFrames; i++) {
				listPaginas.get(i).setR(false);
			}
			clockTick = 0;
		}
	}
}
