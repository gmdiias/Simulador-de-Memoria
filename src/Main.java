import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner leitor = new Scanner(System.in);
		System.out.println("Simulador de Memória Virtual");
		System.out.println("Para iniciar é necessário configurar alguns parametros ...");
		System.out.print("Qual a quantidade de Frames desejada ?? ");
		int numFrames = leitor.nextInt();
		System.out.print("Qual o algoritmo desejado ? (1-NRU / 2-MRU): ");
		int escolha = leitor.nextInt();
		
		if(escolha == 1) {
			AlgoritmoNRU nru = new AlgoritmoNRU(numFrames);
			nru.gerenciadorNRU();
		}
		
		else if(escolha == 2) {
			AlgoritmoFIFO fifo = new AlgoritmoFIFO(numFrames);
			fifo.gerenciadorFIFO();
		}
		
	}
	
}
