package Ejercicio4;

import messagepassing.*;

import java.util.Random;

import messagepassing.MailBox;

/**
 * Hilo controlador que asigna torno, concede acceso y procesa liberaciones.
 */
public class HiloControlador extends Thread{
	private MailBox buzonPeticiones;
	private MailBox[] buzonRespuestas;
	private MailBox buzonLiberacion;
	private MailBox mutexPantalla;
	private Selector sel;
	private MailBox peticionTornoR;
	private MailBox peticionTornoL;
	private boolean tornoRLibre;
	private boolean tornoLLibre;
	private Random rand;
	private int liberacionesProcesadas;
	private static final int TOTAL_VALIDACIONES = 50 * 5;

	
	
	/**
	 * Crea el controlador con todos los buzones compartidos.
	 *
	 * @param buzonPeticiones buzon de peticiones de aficionados
	 * @param buzonRespuestas buzones de respuesta por aficionado
	 * @param buzonLiberacion buzon para liberar torno
	 * @param mutexPantalla buzon usado como mutex de pantalla
	 * @param peticionTornoR buzon de peticiones del torno R
	 * @param peticionTornoL buzon de peticiones del torno L
	 */
	public HiloControlador(MailBox buzonPeticiones, MailBox[] buzonRespuestas, MailBox buzonLiberacion,MailBox mutexPantalla, MailBox peticionTornoR,  MailBox peticionTornoL) {
		super();
		this.buzonPeticiones = buzonPeticiones;
		this.buzonRespuestas = buzonRespuestas;
		this.buzonLiberacion = buzonLiberacion;
		this.mutexPantalla = mutexPantalla;
		this.peticionTornoL = peticionTornoL;
		this.peticionTornoR = peticionTornoR;

		this.rand = new Random();
		this.tornoRLibre = true;
		this.tornoLLibre = true;
		this.liberacionesProcesadas = 0;


		this.sel = new Selector();
		sel.addSelectable(buzonPeticiones, false);
		sel.addSelectable(peticionTornoR, false);
		sel.addSelectable(peticionTornoL, false);
		sel.addSelectable(buzonLiberacion, false);



		
		
	}
	/**
	 * Atiende peticiones de asignacion, entradas a tornos y liberaciones
	 * hasta completar todas las validaciones previstas.
	 */
	@Override
	public void run() {

        mutexPantalla.send("llavePantalla");
		while(true) {
			
		buzonPeticiones.setGuardValue(true);
		peticionTornoR.setGuardValue(tornoRLibre);
		peticionTornoL.setGuardValue(tornoLLibre);
        buzonLiberacion.setGuardValue(!tornoRLibre || !tornoLLibre);
   
			switch (sel.selectOrBlock()) {
			case 1: {
				int id = (int) buzonPeticiones.receive();
				int tEstimado = rand.nextInt(10)+1;
				String torno;
				if(tEstimado > 5) {
					torno = "L";
				}
				else {
					torno = "R";
				}
				RespuestaControlador respuesta = new RespuestaControlador(torno, tEstimado);
				buzonRespuestas[id].send(respuesta);
				break;
				
			}
			case 2: {
				int id = (int) peticionTornoR.receive();
				tornoRLibre = false;
				buzonRespuestas[id].send("llave");
				break;
			}
			case 3:{
				int id = (int) peticionTornoL.receive();
				tornoLLibre = false;
				buzonRespuestas[id].send("llave");
				break;
			}
				
				
		
			case 4: {
				
				String torno = (String) buzonLiberacion.receive();
				liberacionesProcesadas++;
				if(torno.equals("R")) {
					tornoRLibre = true;
			}
				else {
					tornoLLibre = true;
				}
				if (liberacionesProcesadas >= TOTAL_VALIDACIONES) {
					return;
				}
				break;
			}
			}
        }
			
	
			
		}
	}

		
		
	


