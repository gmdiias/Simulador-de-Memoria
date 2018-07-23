import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner leitor = new Scanner(System.in);
		System.out.println("-----------------------------------------------");
		System.out.println("Simulador de Memória Virtual");
		System.out.println("-----------------------------------------------");
		System.out.println("Para iniciar é necessário configurar alguns parametros ...");
		System.out.print("Qual o caminho do arquivo que deseja executar ?? ");
		String caminhoArquivo = leitor.nextLine();
		System.out.print("Qual a quantidade de Frames desejada ?? ");
		int numFrames = leitor.nextInt();
		System.out.print("Qual o tamanho da Página desejada ?? (1 - 1024 | 2 - 2048 | 3 - 4096) ");
		int tamPagina = leitor.nextInt();
		System.out.print("Qual o algoritmo desejado ?? (1 - NRU | 2 - FIFO): ");
		int escolha = leitor.nextInt();
		
		if(tamPagina == 1) {
			tamPagina = 1024;
		}
		else if(tamPagina == 2) {
			tamPagina = 2048;
		}
		else if(tamPagina == 3) {
			tamPagina = 4096;
		}
		else {
			System.out.println("Tamanho de página inválido, setado valor default 1024.");
			tamPagina = 1024;
		}
		
		if(escolha == 1) {
			AlgoritmoNRU nru = new AlgoritmoNRU(caminhoArquivo, numFrames, tamPagina);
			nru.gerenciadorNRU();
		}
		
		else if(escolha == 2) {
			AlgoritmoFIFO fifo = new AlgoritmoFIFO(caminhoArquivo, numFrames, tamPagina);
			fifo.gerenciadorFIFO();
		}
		
	}
	
}
