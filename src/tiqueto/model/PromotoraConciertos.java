package tiqueto.model;

import java.util.concurrent.ThreadLocalRandom;
import tiqueto.EjemploTicketMaster;
public class PromotoraConciertos extends Thread {

	final WebCompraConciertos webCompra;

	public PromotoraConciertos(WebCompraConciertos webCompra) {
		super();
		this.webCompra = webCompra;
	}

	@Override
	public void run() {
//Bucle en el que hasta que no se acaben las entradas
		while (webCompra.entradastotales > 0) {
			//Introduzco sentencia synchronized para que no se descuadre con el funcionamiento de WebCompra
			synchronized (webCompra) {
				//si no hay entradas las repone el promotor
				if (!webCompra.hayEntradas()) {
					mensajePromotor("Voy a reponer entradas que no quedan");
					int entradasrepo = webCompra.entradasreposicion;
					webCompra.reponerEntradas(entradasrepo);


					try {
						///después de reponer se va a dormir el promotor entre 3 y 8
						Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 8000));
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						System.out.println("Thread interrupted: " + e);
					}
				}
				//Notifica si se acabaron las entradas y cierra la venta
				if (webCompra.entradastotales == 0){
					mensajePromotor("Se acabaron las entradas. Toca cerrar venta");
					webCompra.cerrarVenta();
				}
			}

		}
	}
	
	/**
	 * Método a usar para cada impresión por pantalla
	 * @param mensaje Mensaje que se quiere lanzar por pantalla
	 */
	private void mensajePromotor(String mensaje) {
		//Introduzco sentencia synchronized para que no se descuadre con el funcionamiento de WebCompra
		synchronized (webCompra) {
			System.out.println(System.currentTimeMillis() + "| Promotora: " + mensaje);
		}
	}
}