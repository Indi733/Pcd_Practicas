package Ejercicio2;

import java.util.concurrent.Semaphore;

/**
 * Programa principal del Ejercicio 2.
 * Inicializa paneles y lanza los 10 hilos de equipos.
 */
public class Programa2 {
	static Semaphore disponibles = new Semaphore(3);
	static Semaphore mutex = new Semaphore(1);
	static volatile boolean[] libres = { true, true, true };
	static Panel panel1 = new Panel("Panel 0", 100, 100);
	static Panel panel2 = new Panel("Panel 1", 550, 100);
	static Panel panel3 = new Panel("Panel 2", 1000, 100);
	static Panel[] paneles = { panel1, panel2, panel3 };

	public static void main(String[] args) {

		Thread[] equipos = new Thread[10];

		for (int i = 0; i < 10; i++) {
			equipos[i] = new HiloEquipo(i);
			equipos[i].start();
		}

	}

}
