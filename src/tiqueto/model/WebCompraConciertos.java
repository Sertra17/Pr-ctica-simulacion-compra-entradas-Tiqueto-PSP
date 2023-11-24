package tiqueto.model;

import tiqueto.IOperacionesWeb;

import static tiqueto.EjemploTicketMaster.MAX_ENTRADAS_POR_FAN;

public class WebCompraConciertos implements IOperacionesWeb{
	// Declaración de variables de clase
	public int entradasalaventa;
	public int entradastotales;
	public int entradasreposicion;

	// Constructor para WebCompraConciertos
	public WebCompraConciertos() {
		super();
	}
	@Override
	public synchronized boolean comprarEntrada() {
		// TODO Auto-generated method stub
		boolean comprada = true;
// Comprobando si hay entradas disponibles
			if (hayEntradas()) {
				// Si hay disponibles, decrementar el contador y mostrar un mensaje
				entradasalaventa--;
				mensajeWeb("Entrada comprada. Quedan: " + entradasalaventa + " a la venta");
			}
			else {
				// Si no hay entradas disponibles, establecer devolverá false
				comprada = false;
			}

		return comprada;
	}

@Override
public synchronized int reponerEntradas(int numeroEntradas) {
	// Verificar si es necesario reponer
	if (entradasalaventa == 0 && entradasreposicion > 0) {
		entradasreposicion = numeroEntradas;
		if (entradasreposicion >= entradastotales) {
			// Calcular cuántas entradas se pueden reponer
			int entradasAReponer = Math.min(numeroEntradas, entradastotales);

			// Añadir las entradas a la venta y restarlas del total
			entradasalaventa += entradasAReponer;
			entradastotales -= entradasAReponer;

			// Actualizar el número de entradas a reponer
			entradasreposicion -= entradasAReponer;

			mensajeWeb("Entradas repuestas");

		}
		else {
			// Si el número de entradas a reponer es menor que el total
			entradasalaventa = entradasalaventa + numeroEntradas;
			entradastotales = entradastotales - entradasalaventa;
			mensajeWeb("Entradas repuestas");
		}
	} else {
		// Mostrar un mensaje si hay entradas disponibles o no es necesario reponer
		mensajeWeb("Ya hay entradas disponibles o no hay necesidad de reponerlas por que no quedan");
	}
	return entradasalaventa;
}

	@Override
	public synchronized void cerrarVenta() {
		// TODO Auto-generated method stub
		mensajeWeb("Venta cerrada. No se pueden comprar más entradas.");
		// Notificar a todos los hilos que estén esperando
		notifyAll();
	}


	@Override
	public synchronized boolean hayEntradas() {
		// TODO Auto-generated method stub
		boolean hay;
		// Verificar si hay entradas disponibles
			if (entradasalaventa == 0){
				hay = false;
			}
			else {
				hay = true;
			}
		// Notificar a todos los hilos que estén esperando
		notifyAll();
		return hay;
	}


	@Override
	public synchronized int entradasRestantes() {
		// TODO Auto-generated method stub
		// Mostrar un mensaje indicando cuántas entradas restantes hay
		mensajeWeb("QUEDAN " + entradastotales + " entradas restantes");
		return entradastotales;
	}

	/**
	 * Método a usar para cada impresión por pantalla
	 * @param mensaje Mensaje que se quiere lanzar por pantalla
	 */
	private void mensajeWeb(String mensaje) {
		synchronized (System.out) {
			System.out.println(System.currentTimeMillis() + "| WebCompra: " + mensaje);
		}
	}

}