package Ejercicio3;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Monitor que gestiona entrada y salida en las zonas del gimnasio
 * y el uso de la bicicleta premium de la zona de cardio.
 */
public class MonitorZonas {

	private final static int TIEMPO_PROMEDIO = 9;
	private ReentrantLock cerrojo;
	private Condition[] zona;
	private Condition biciPremium;
	private int[] colas;
	private boolean[] ocupado;
	private boolean biciPremiumOcupada;
	private Random random;
	
	/**
	 * Inicializa el monitor de zonas y sus colas.
	 */
	public MonitorZonas() {
		this.cerrojo = new ReentrantLock();
		this.zona = new Condition[] {
			cerrojo.newCondition(),
			cerrojo.newCondition(),
			cerrojo.newCondition(),
			cerrojo.newCondition()
		};
		this.biciPremium = cerrojo.newCondition();
		this.colas = new int[] {0, 0, 0, 0};
		this.ocupado = new boolean[] {false, false, false, false};
		this.biciPremiumOcupada = false;
		this.random = new Random();
	}
	
	
	/**
	 * Entra en una zona; espera si esta ocupada.
	 *
	 * @param idZona identificador de la zona
	 */
	public void entrar(int idZona) {
		cerrojo.lock();
		try {
			while(ocupado[idZona]){
				colas[idZona]++;        
			    zona[idZona].await();
			    colas[idZona]--;
			}
			
			
			ocupado[idZona] = true;
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			cerrojo.unlock();
		}
	}
	
	/**
	 * Solicita la bicicleta premium y espera si esta ocupada.
	 */
	public void pedirBiciPremium() {
		cerrojo.lock();
		try {
			while(biciPremiumOcupada) {
				biciPremium.await();
			}
			biciPremiumOcupada = true;
			
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			cerrojo.unlock();
		}
	}
	
	/**
	 * Elige una zona libre al azar; si no hay libres, elige la de menor cola.
	 *
	 * @return identificador de la zona elegida
	 */
	public int elegirZona() {
		cerrojo.lock();
		int zonaElegida = -1;
		try {
			ArrayList<Integer> zonasLibres = new ArrayList<>();
			for(int i = 0; i < 4; i++) {
				if(!ocupado[i]) {
					zonasLibres.add(i);
				}
			}
			
			if (!zonasLibres.isEmpty()) {
	            return zonasLibres.get(random.nextInt(zonasLibres.size()));
	        }else {
	        	zonaElegida = 0;
	            for (int i = 1; i < 4; i++) {
	                if (colas[i] < colas[zonaElegida]) { 
	                    zonaElegida = i;
	                }
	            }
	        	
	        	return zonaElegida;
	        }	
		
		}finally {
			cerrojo.unlock();
		}	
	}
	
	
	/**
	 * Devuelve una estimacion simple del tiempo de espera por zona.
	 *
	 * @return array con las 4 estimaciones
	 */
	public int[] getEstimaciones(){
		cerrojo.lock();
		try {
			int[] estimaciones = new int[4];
			for(int i = 0; i < 4; i++) {
				if(ocupado[i])
					estimaciones[i] = TIEMPO_PROMEDIO * colas[i];
				else
					estimaciones[i] = 0;
			}
			return estimaciones;
		}finally {
			cerrojo.unlock();
		}
	}
	
	
	/**
	 * Sale de la zona y, si aplica, libera la bicicleta premium.
	 *
	 * @param idZona identificador de la zona
	 * @param usoBici indica si el cliente uso la bicicleta premium
	 */
	public void salir(int idZona, boolean usoBici) {
		cerrojo.lock();
		try {
			if((idZona == 0) && (usoBici)) {
				biciPremiumOcupada = false;
				biciPremium.signal();
			}
			ocupado[idZona] = false;
			zona[idZona].signal();
		}finally {
			cerrojo.unlock();
		}
	
	}

	
	

}
