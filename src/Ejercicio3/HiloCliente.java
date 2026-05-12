package Ejercicio3;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hilo que simula el recorrido de un cliente por torno y zona de entrenamiento.
 */
public class HiloCliente extends Thread {
	
	private final static int CARDIO = 0;
	private int id;
	private ReentrantLock consola;
	private MonitorTorno monitorTorno;
	private MonitorZonas monitorZonas;
	
	 /**
	  * Crea un cliente con los monitores compartidos.
	  *
	  * @param id identificador del cliente
	  * @param monitorTorno monitor para acceso a tornos
	  * @param monitorZonas monitor para gestion de zonas
	  * @param consola cerrojo para escribir por pantalla sin mezclar salida
	  */
	 public HiloCliente(int id, MonitorTorno monitorTorno, MonitorZonas monitorZonas,
			 			ReentrantLock consola) {
		 super();
		 this.id = id;
		 this.monitorTorno = monitorTorno;
		 this.monitorZonas = monitorZonas;
		 this.consola = consola;
	 }
	
	/**
	 * Ejecuta el flujo del cliente: torno, eleccion de zona, posible bici premium,
	 * entrenamiento y salida.
	 */
	@Override
	public void run(){
		Random random = new Random();
		int X = (random.nextInt(5) + 1) * 1000;      // 1-5 segundos en torno
		int Y = (random.nextInt(91) + 30) * 100;      // 3-12 segundos entrenando

        
		//FASE 1: TORNO
		int miTorno;
		try {
			miTorno = monitorTorno.entrar(id);
			Thread.sleep(X);
			monitorTorno.salir(miTorno);
		
		//FASE 2: Elejir zona + imprimir
		int zona = monitorZonas.elegirZona(); //Elegimos zona 
		
		int estimaciones[] = monitorZonas.getEstimaciones();
		
		consola.lock();
		try {
		System.out.println("--------------------------------------------------------------");
		System.out.println("Cliente " + id + " ha pasado por el torno: " + miTorno);
	    System.out.println("Tiempo en el torno (acceso): " + X);
	    System.out.println("Zona elegida: " + zona);
	    System.out.println("Tiempo de entrenamiento: " + Y);
	    System.out.println("Estimación de espera (sin incluirse a sí mismo):");
	    System.out.println("  Zona1(Cardio)=" + estimaciones[0] + ", Zona2(Fuerza)=" + estimaciones[1]
	            + ", Zona3(Funcional)=" + estimaciones[2] + ",");
	    System.out.println("  Zona4(Estiramientos)=" + estimaciones[3]);
	    if (zona == CARDIO) System.out.println("Espera bicicleta premium (si aplica)=0");
		System.out.println("--------------------------------------------------------------");
		} finally {
			consola.unlock();
		}
		
		
	    //FASE 3: Entrar en zona
	    monitorZonas.entrar(zona);
	    
	    //FASE 4: Bicicleta premium
	    boolean usoBici = false;
	    if (zona == CARDIO && random.nextDouble() < 0.30){
	    	monitorZonas.pedirBiciPremium();
	    	usoBici = true;
	    	
	    }
	    
	    //FASE 5: Entrenar
	    Thread.sleep(Y);
	    
	    //FASE 6: Salir
	    monitorZonas.salir(zona, usoBici);
	    
		} catch (InterruptedException e) {
            e.printStackTrace();
        }
	    
	}
	
	
	
	
}

