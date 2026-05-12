package Ejercicio4;

import java.util.Random;

import messagepassing.MailBox;

/**
 * Hilo que representa a un aficionado.
 * En cada vuelta solicita torno, valida entrada y libera el torno.
 */
public class HiloAficionado extends Thread {

	private final int id;
	private MailBox buzonPeticiones;
	private MailBox miBuzon;
	private MailBox peticionTornoR;
	private MailBox peticionTornoL;
	private MailBox buzonLiberacion;
	private MailBox mutexPantalla;
	private Random rand;
	
	

	/**
	 * Crea un aficionado y sus buzones de comunicacion.
	 *
	 * @param id identificador del aficionado
	 * @param buzonPeticiones buzon para pedir asignacion de torno
	 * @param miBuzon buzon personal para recibir respuesta del controlador
	 * @param buzonLiberacion buzon para indicar liberacion de torno
	 * @param mutexPantalla buzon usado como exclusion mutua de pantalla
	 * @param peticionTornoR buzon de peticiones del torno R
	 * @param peticionTornoL buzon de peticiones del torno L
	 */
	public HiloAficionado(int id, MailBox buzonPeticiones, MailBox miBuzon,MailBox buzonLiberacion, MailBox mutexPantalla, MailBox peticionTornoR, MailBox peticionTornoL) {
		super();
		this.id = id;
		this.buzonPeticiones = buzonPeticiones;
		this.miBuzon = miBuzon;
		this.buzonLiberacion = buzonLiberacion;
		this.mutexPantalla = mutexPantalla;
	    this.peticionTornoL = peticionTornoL;
        this.peticionTornoR = peticionTornoR;
		this.rand = new Random();
		
	}



	/**
	 * Ejecuta 5 ciclos de salida y reentrada, coordinados por paso de mensajes.
	 */
	@Override
	public void run() {
		try {
		for(int i = 0; i<5; i++) {
			Thread.sleep(rand.nextInt(500)+50);
			buzonPeticiones.send(id);
			RespuestaControlador respuesta;
			respuesta = (RespuestaControlador) miBuzon.receive();
			if(respuesta.getTorno().equals("R")) {
				peticionTornoR.send(id);
			}
			else {
				peticionTornoL.send(id);
			}
			miBuzon.receive();
			Thread.sleep(respuesta.getTiempoEstimado()*100);
			buzonLiberacion.send(respuesta.getTorno());
			
			String llavePantalla = (String) mutexPantalla.receive();
			System.out.println("Aficionado " + this.id + " ha usado la cola " + respuesta.getTorno());
			System.out.println("Tiempo de validación = " + respuesta.getTiempoEstimado());
			System.out.println("Thread.sleep(" + respuesta.getTiempoEstimado() + ")");
			System.out.println("Aficionado " + this.id + " liberando la cola " + respuesta.getTorno()+ "\n");
			mutexPantalla.send(llavePantalla);

		}
	}
		catch (InterruptedException e) {
			e.printStackTrace();		
			}
		
	}
	
}
