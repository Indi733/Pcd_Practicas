package Ejercicio1;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hilo que genera una matriz 3x3, calcula A + A y la muestra por pantalla.
 * La salida se protege con un cerrojo para evitar mezclas en consola.
 */
public class HiloSuma extends Thread {

	 ReentrantLock micerrojo;
	 Random rand = new Random();

	 
	/**
	 * Crea el hilo de suma con el cerrojo compartido.
	 *
	 * @param cerrojo cerrojo usado para proteger la salida por pantalla
	 */
	public HiloSuma (ReentrantLock cerrojo) {
		super();
		this.micerrojo = cerrojo;
	}

	/**
	 * Ejecuta 10 repeticiones: genera la matriz, calcula 2A e imprime el resultado.
	 */
	@Override
	public void run() {
		super.run();
		for(int reps = 0; reps < 10; reps++) {
		int[][] matriz = new int[3][3];
		int[][] matrizRes = new int[3][3];

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j<3; j++) {
				matriz[i][j] = rand.nextInt(100);
			}
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j<3; j++) {
				matrizRes[i][j] = matriz[i][j]+matriz[i][j];
				
			}
		}
		micerrojo.lock();
		try {
			System.out.println("A + A \n");
			for(int a = 0; a<3; a++) {
					System.out.println(matriz[a][0]+ " " + matriz[a][1] + " " + matriz[a][2]);
			}
			System.out.println("+");

			for(int a = 0; a<3; a++) {
				System.out.println(matriz[a][0] + " " + matriz[a][1] + " "+ matriz[a][2]);
		}
			System.out.println(" ");
			System.out.println("2A");
			for(int a = 0; a<3; a++) {
				System.out.println(matrizRes[a][0] + " " + matrizRes[a][1] + " "+ matrizRes[a][2]);
		}
			System.out.println("\n");

			
		}
			
			 finally {
				micerrojo.unlock();
			}
		
		}
		
	}

	
	
	
	
	
	
	
}
