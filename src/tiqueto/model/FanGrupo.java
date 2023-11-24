package tiqueto.model;

import java.util.concurrent.ThreadLocalRandom;

import static tiqueto.EjemploTicketMaster.MAX_ENTRADAS_POR_FAN;
import static tiqueto.EjemploTicketMaster.TOTAL_ENTRADAS;

public class FanGrupo extends Thread {

	final WebCompraConciertos webCompra;
	int numeroFan;
	private String tabuladores = "\t\t\t\t";
	int entradasCompradas = 0;

	public FanGrupo(WebCompraConciertos web, int numeroFan) {
		super();
		this.numeroFan = numeroFan;
		this.webCompra = web;
	}
	@Override
	public void run() {
//El fan se quedará esperando mientras no haya alcanzado su limite y queden entradas tanto sisponible como a la venta
		while ((entradasCompradas < MAX_ENTRADAS_POR_FAN) && ((TOTAL_ENTRADAS > 0) || (webCompra.hayEntradas()))) {
			synchronized (webCompra) {
				//Si hay entradas se compran
				if (webCompra.hayEntradas()) {
					mensajeFan("Intento comprar una entrada");
					//si retorna true se compra la entrada
					webCompra.comprarEntrada();
						mensajeFan("He comprado una entrada");
						entradasCompradas++;

				} else {
					mensajeFan("Me espero a que repongan");
				}
			}

			// Verificar si se ha cerrado la venta o se alcanzó el límite de entradas por grupo
			if (entradasCompradas == MAX_ENTRADAS_POR_FAN) {
				mensajeFan("He alcanzado el límite de entradas. No puedo comprar más.");
				break;
			}
			//Verificar si aunque se haya quedado esperando el fan a que repongan no hay entradas entonces se pira de la cola
			if (webCompra.entradasalaventa == 0 && webCompra.entradastotales == 0){
				mensajeFan("Ya no quedan entradas es imposible comprarlas me las piro");
				break;
			}
			try {
				///después de comprar se va a dormir el fan entre 1 y 3 segundos
				Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Thread interrupted: " + e);
			}
		}
	}

	public void dimeEntradasCompradas() {
		mensajeFan("Sólo he conseguido: " + entradasCompradas);
	}
	
	/**
	 * Método a usar para cada impresión por pantalla
	 * @param mensaje Mensaje que se quiere lanzar por pantalla
	 */
	private void mensajeFan(String mensaje) {
//Introduzco sentencia synchronized para que no se descuadre con el funcionamiento de WebCompra
		synchronized (webCompra) {
			System.out.println(System.currentTimeMillis() + "|" + tabuladores + " Fan " + this.numeroFan + ": " + mensaje);
		}
	}
}