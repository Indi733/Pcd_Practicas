package Ejercicio4;

import java.io.Serializable;

/**
 * Mensaje de respuesta del controlador con torno asignado y tiempo estimado.
 */
public class RespuestaControlador implements Serializable {

private static final long serialVersionUID = 1L;
private String torno;
private int tiempoEstimado;

	/**
	 * Crea una respuesta del controlador.
	 *
	 * @param torno torno asignado (R o L)
	 * @param tiempoEstimado tiempo estimado de validacion
	 */
public RespuestaControlador(String torno, int tiempoEstimado) {
	super();
	this.torno = torno;
	this.tiempoEstimado = tiempoEstimado;
}

/**
 * Devuelve el torno asignado.
 *
 * @return torno asignado
 */
public String getTorno() {
	return torno;
}

/**
 * Devuelve el tiempo estimado de validacion.
 *
 * @return tiempo estimado
 */
public int getTiempoEstimado() {
	return tiempoEstimado;
}



	
}
