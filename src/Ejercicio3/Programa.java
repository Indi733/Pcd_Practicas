package Ejercicio3;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Programa principal del Ejercicio 3.
 * Crea monitores compartidos y lanza 50 hilos cliente.
 */
public class Programa {

	public static void main(String[] args) {

		MonitorTorno monitorTorno = new MonitorTorno();
		MonitorZonas monitorZonas = new MonitorZonas();
		ReentrantLock consola = new ReentrantLock();

		for (int i = 0; i < 50; i++) {
			new HiloCliente(i, monitorTorno, monitorZonas, consola).start();
		}
	}

}
