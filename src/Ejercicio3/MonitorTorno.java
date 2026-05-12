package Ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Monitor que gestiona el acceso a los 3 tornos del gimnasio.
 */
public class MonitorTorno {
	
	private ReentrantLock cerrojo;
	private Condition[] colaTorno;
	private boolean[] ocupado;
	private int[] enCola;
	
	/**
	 * Inicializa el estado del monitor de tornos.
	 */
	public MonitorTorno() {
		this.cerrojo = new ReentrantLock();
		this.colaTorno = new Condition[] {
			cerrojo.newCondition(),
			cerrojo.newCondition(),
			cerrojo.newCondition()
		};
		this.ocupado = new boolean[] {false, false, false};
		this.enCola = new int[] {0, 0, 0};
	}
	
		
	/**
	 * Asigna un torno al cliente.
	 * Si no hay tornos libres, espera en la cola del torno con menor espera.
	 *
	 * @param id identificador del cliente
	 * @return identificador del torno asignado
	 */
	public int entrar(int id) {
	    cerrojo.lock();
	    int tornoElegido = -1;
	    try {
	        // 1. Buscar primer torno libre
	        for (int i = 0; i < 3; i++) {
	            if (!ocupado[i]) {          
	                tornoElegido = i;
	                break;
	            }
	        }

	        // 2. Si no hay libre elegir el de menor cola
	        if (tornoElegido == -1) {   	
	            tornoElegido = 0;
	            for (int i = 1; i < 3; i++) {
	                if (enCola[i] < enCola[tornoElegido]) {
	                    tornoElegido = i;
	                }
	            }
		        // 3. Esperar en la cola del torno elegido
		        enCola[tornoElegido]++;
		        while (ocupado[tornoElegido]) {
		            colaTorno[tornoElegido].await();
		        }
		        enCola[tornoElegido]--;
	        }
	        // 4. Reclamar el torno
	        ocupado[tornoElegido] = true;
	        return tornoElegido;
	        
	    } catch (InterruptedException e) {
	        throw new RuntimeException(e);
	    } finally {
	        cerrojo.unlock();
	    }
	    
	}
	
	/**
	 * Libera un torno y despierta a un cliente que espera en su cola.
	 *
	 * @param tornoid identificador del torno a liberar
	 */
	public void salir(int tornoid) {
		cerrojo.lock();
		try{
			ocupado[tornoid] = false;
			colaTorno[tornoid].signal();
		}finally {
			cerrojo.unlock();
		}
	}
		
	
	

}
