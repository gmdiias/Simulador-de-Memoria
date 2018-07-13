import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Main {
	
	HashMap<Integer, Integer> frame = new HashMap<>();
	List<Pagina> listPaginas = new ArrayList<>(5);
	
	int contPF = 0;
	int modificado = 0;

	public static void main(String[] args) {
		System.out.println("Simulador de Memória Virtual");
		
		Main main = new Main();
		
		main.inicializaList();
		main.lerArquivo();
		
		
		
	}
	
	public void lerArquivo() {
		BufferedReader br;
		try {
			FileReader ler = new FileReader("./entrada.txt");
			BufferedReader reader = new BufferedReader(ler);
			String linha;
			
			int instrucao;
			String tipo;
			int dado;

			while (!(linha = reader.readLine()).equals("#eof")) {
				linha = linha.replace(":", "");
				String[] texto = linha.split(" ");
				instrucao = Integer.parseInt(texto[0].substring(2, 9), 16)/1024;
				tipo = texto[1];
				dado = Integer.parseInt(texto[2].substring(2, 9), 16)/1024;
				
				verificaHash(instrucao);
				System.out.println(linha);
			}

		} catch (IOException e) {
			System.err.println("Arquivo de dados não encontrado, verifique o caminho ...");
			e.printStackTrace();
		}
	}
	
	public void inicializaList() {
		for(int i = 0; i < 5; i ++) {
			Pagina nova = new Pagina();
			listPaginas.add(nova);
		}
	}

	public void verificaHash(int posicao) {
		if(frame.get(posicao) == null) {
			frame.put(posicao, -1);

		}
		
		if(frame.get(posicao) == -1) {
			algoritmoNRU(posicao);
		}
		else {
			//todo ATUALIZA DADOS ...
		}
	}
	
	public void algoritmoNRU(int posicao) {
		for(int i = 0; i < 5; i++) {
			Pagina pagina = listPaginas.get(i);
			if(pagina.getNumPag() == -1) {
				pagina.setNumPag(posicao);
				pagina.setR(true);
				pagina.setW(false);
				frame.replace(posicao, i);
				return;
			}
		}
	}
	
	
}
