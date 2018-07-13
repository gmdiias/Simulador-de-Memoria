import java.awt.Container;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	HashMap<Integer, Integer> frame = new HashMap<>();
	List<Pagina> listPaginas = new ArrayList<>();
	
	int contPF = 0;
	int modificado = 0;
	int numFrames = 0;

	public static void main(String[] args) {
		Main main = new Main();
		Scanner leitor = new Scanner(System.in);
		System.out.println("Simulador de Memória Virtual");
		System.out.println("Para iniciar é necessário configurar alguns parametros ...");
		System.out.println("Qual a quantidade de Frames desejada ?? ");
		main.numFrames = leitor.nextInt();
		
		main.inicializaList();
		main.lerArquivo();
		
		System.out.println("Quantidade de PF: " + main.contPF);
		
		
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
				tipo = texto[1].toLowerCase();
				dado = Integer.parseInt(texto[2].substring(2, 9), 16)/1024;
				
				verificaHash(instrucao, "r");
				verificaHash(dado, tipo);
				System.out.println(linha);
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
		
		//TODO realizar verificação de classe Tipo 0 (0,0)
		//TODO classe Tipo 1 (1,0)
		//TODO classe Tipo 2 (0,1)
		//TODO classe Tipo 3 (1,1)
	}
	
	public void refreshPage(int posicao, String tipo) {
		int id = frame.get(posicao);
		listPaginas.get(id).setR(true);
		if(tipo.equals("w")) {
			listPaginas.get(id).setW(true);
		}
	}	
	
	public void realizaTrocaPaginas() {
		//TODO implementar realizador de troca de paginas
	}
	
	//TODO Refatorar classe, adicionando a uma outra classe, removendo os metodos da principal ...
}
