package Ejercicio2;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Hilo que representa a un equipo.
 * En cada repeticion genera resultados, construye la matriz resumen R
 * y la muestra en un panel libre.
 */
public class HiloEquipo extends Thread {
	
	Semaphore disponibles;
	int idequipo;
	

	/**
	 * Crea el hilo de un equipo.
	 *
	 * @param idequipo identificador del equipo
	 */
	public HiloEquipo(int idequipo) {
		super();
		this.idequipo = idequipo;
	}

	/**
	 * Ejecuta 3 ciclos de trabajo del equipo y usa semaforos para acceder
	 * a un panel compartido.
	 */
	@Override
	public void run() {
		int aacceder= 0;
		for(int reps = 0; reps <3; reps++) {
		int[][] deportista1 = new int[5][5];
		int[][] deportista2 = new int[5][5];
		int[][] deportista3 = new int[5][5];
		int[][] deportista4 = new int[5][5];
		int[][][] deportistas = {deportista1, deportista2, deportista3, deportista4};
		int[][] R = new int[4][5];
		Random rand = new Random();
		
		
	for(int a = 0; a < 4; a++) {
		for(int i = 0; i < 5; i++) {
			deportistas[a][0][i] = rand.nextInt(61)+90; //Decimas de s
			deportistas[a][1][i] = rand.nextInt(401)+400; // cm
			deportistas[a][2][i] = rand.nextInt(71)+10;  // metros
			deportistas[a][3][i] = rand.nextInt(301)+300; // segundos
			deportistas[a][4][i] = rand.nextInt(151)+150; //decimas de s
		}
	}
	
	
	for(int i = 0; i < 4; i++) {
		R[i][0] = calculaMin(deportistas[i][0]);
		R[i][1] = calculaMax(deportistas[i][1]);
		R[i][2] = calculaMax(deportistas[i][2]);
		R[i][3] = calculaMin(deportistas[i][3]);
		R[i][4] = calculaMin(deportistas[i][4]);
		}
		
	
	
	try {
		Programa2.disponibles.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	 	try {
			Programa2.mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	
	 	
		for(int i = 0;  i < 3; i++) {
			if(Programa2.libres[i] == true) {
				aacceder = i;
				Programa2.libres[i] = false;
				break;
			}
		}
			Programa2.mutex.release();
			
			String textoR = generarStringMatriz(R);
			Programa2.paneles[aacceder].escribir_mensaje("Usando panel " + aacceder + " el hilo (equipo) " + idequipo + "\n");
			Programa2.paneles[aacceder].escribir_mensaje("Matriz R (resumen del equipo " + idequipo + ": mejores marcas) =\n" + textoR);
			Programa2.paneles[aacceder].escribir_mensaje("Terminando de usar panel " + aacceder + " el hilo (equipo) " + idequipo + "\n\n");
		
		
		 	try {
				Programa2.mutex.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 	Programa2.libres[aacceder] = true;
			Programa2.mutex.release();

		 	Programa2.disponibles.release();
		}
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
			}
		
		
		
		
		
		
	
	/**
	 * Devuelve el valor maximo de un array.
	 *
	 * @param array array de entrada
	 * @return valor maximo
	 */
	private int calculaMax(int[] array) {
		int aux = array[0];
		for(int i = 1; i < array.length; i++) {
			if(aux < array[i]) {
				aux = array[i];
			}
		}
		return aux;
	}
	
	/**
	 * Devuelve el valor minimo de un array.
	 *
	 * @param array array de entrada
	 * @return valor minimo
	 */
	private int calculaMin(int[] array) {
		int aux = array[0];
		for(int i = 1; i < array.length; i++) {
			if(aux > array[i]) {
				aux = array[i];
			}
		}
		return aux;
	}
	/**
	 * Convierte una matriz en texto para mostrarla por panel.
	 *
	 * @param matriz matriz a convertir
	 * @return texto de la matriz con saltos de linea
	 */
	private String generarStringMatriz(int[][] matriz) {
	    StringBuilder sb = new StringBuilder();
	    
	    for (int i = 0; i < matriz.length; i++) {
	        for (int j = 0; j< matriz[i].length; j++) {
	            sb.append(matriz[i][j]).append("\t");
	        }
	        sb.append("\n");
	    }
	    
	    return sb.toString();
	}
	
	

}
