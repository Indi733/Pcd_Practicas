package Ejercicio1;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Programa principal del Ejercicio 1.
 * Lanza un hilo de multiplicacion y otro de suma usando un mismo cerrojo.
 */
public class Programa1 {

	public static void main(String[] args) {
		ReentrantLock cerrojo = new ReentrantLock();

		HiloMult mult = new HiloMult(cerrojo);
		HiloSuma sum = new HiloSuma(cerrojo);
		mult.start();
		sum.start();

	}

}
