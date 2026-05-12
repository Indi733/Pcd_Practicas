package Ejercicio4;

import messagepassing.MailBox;

/**
 * Programa principal del Ejercicio 4.
 * Crea los buzones, lanza el controlador y los 50 hilos de aficionados.
 */
public class Programa4 {

    public static void main(String[] args) {
        int NUM_AFICIONADOS = 50;

        MailBox buzonPeticiones = new MailBox();
        MailBox buzonLiberacion = new MailBox();
        MailBox mutexPantalla = new MailBox();
        MailBox peticionTornoR = new MailBox();
        MailBox peticionTornoL = new MailBox();

        MailBox[] buzonesAficionados = new MailBox[NUM_AFICIONADOS];
        for (int i = 0; i < NUM_AFICIONADOS; i++) {
            buzonesAficionados[i] = new MailBox();
        }
        Thread controlador = new HiloControlador(buzonPeticiones, buzonesAficionados, buzonLiberacion, mutexPantalla,
                peticionTornoR, peticionTornoL);
        controlador.start();
        Thread[] aficionados = new Thread[NUM_AFICIONADOS];
        for (int i = 0; i < NUM_AFICIONADOS; i++) {
            aficionados[i] = new HiloAficionado(i, buzonPeticiones, buzonesAficionados[i], buzonLiberacion,
                    mutexPantalla, peticionTornoR, peticionTornoL);
            aficionados[i].start();
        }

    }
}
