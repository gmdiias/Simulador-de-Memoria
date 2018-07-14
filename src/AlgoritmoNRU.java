import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlgoritmoNRU {
	HashMap<Integer, Integer> frame = new HashMap<>();
	List<Pagina> listPaginas = new ArrayList<>();
	
	private int contPF = 0;
	private int modificado = 0;
	private int numFrames = 0;
	private int clockTick = 0;
	
	public AlgoritmoNRU() {
		
	}
	
	public AlgoritmoNRU(int numFrames) {
		this.numFrames = numFrames;
	}
	
	public void gerenciadorNRU() {
		inicializaList();
		lerArquivo();
		System.out.println(contPF);
	}
	
	public void lerArquivo() {
		BufferedReader br;
		try {
			FileReader ler = new FileReader("./entrada3.txt");
			BufferedReader reader = new BufferedReader(ler);
			String linha;
			
			int instrucao;
			String tipo;
			int dado;

			while (!(linha = reader.readLine()).equals("#eof")) {
				linha = linha.replace(":", "");
				String[] texto = linha.split(" ");
				instrucao = Integer.parseInt(texto[0].substring(2, 9), 16)/1024;
				tipo = texto[1].toLowerCase();
				dado = Integer.parseInt(texto[2].substring(2, 9), 16)/1024;
				
				verificaHash(instrucao, "r");
				verificaHash(dado, tipo);
				System.out.println(instrucao + " " + tipo + " " + dado);
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
			//TODO AINDA NÂO ESTÀ NA MEMORIA
		}
		else {
			refreshPage(posicao, tipo);
			//TODO ATUALIZA DADOS ...
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
		pagina.setNumPag(posicao);
		pagina.setR(true);
		pagina.setW(false);
		if(tipo.equals("w")) {
			pagina.setW(true);
		}
		frame.replace(posicao, altera);
		//TODO realiza verifica pg
		//TODO realizar verificação de classe Tipo 0 (0,0)
		//TODO classe Tipo 1 (1,0)
		//TODO classe Tipo 2 (0,1)
		//TODO classe Tipo 3 (1,1)
	}
	
	public int verificaClasses() {
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(!pagina.isR() && !pagina.isW()) {
				System.out.println("Tipo 0 encontrado");
				return i;
			}
		}
		
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(!pagina.isR() && pagina.isW()) {
				System.out.println("Tipo 1 encontrado");
				return i;
			}
		}
		
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(pagina.isR() && !pagina.isW()) {
				System.out.println("Tipo 2 encontrado");
				return i;
			}
		}
		
		for(int i = 0; i < numFrames; i++) {
			Pagina pagina = listPaginas.get(i);
			if(pagina.isR() && pagina.isW()) {
				System.out.println("Tipo 3 encontrado");
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
			System.out.println("Limpei");
		}
	}
	
	//TODO Refatorar classe, adicionando a uma outra classe, removendo os metodos da principal ...
	
}
