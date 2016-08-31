package parte2;


public class MonitorPrioridadEscritores implements MonitorArbitraje {

	private int numLectores = 0;
	private boolean escritor = false;
	private int escritoresEnEspera = 0;

	public synchronized void entrarLeer(int numHilo) throws InterruptedException {
		
		while (escritor || escritoresEnEspera > 0){	//mientras se esté escribiendo o haya algún escritor esperando su turno espera
			wait();
		}
		
		numLectores++;	//ahora hay un lector más
		
		System.out.println("Lector " + numHilo + " va a empezar a leer");
	}

	public synchronized void salirLeer(int numHilo) {
		System.out.println("Lector " + numHilo + " ha terminado de leer");
		
		numLectores--;	//ahora hay un lector menos
		if(numLectores <= 0)	//si era el último lector que quedaba (y ya no queda ninguno más) notificar para que los escritores puedan escribir
			notifyAll();
		
	}

	public synchronized void entrarEscribir(int numHilo) throws InterruptedException {
		while(numLectores > 0){	// Espera a que terminen los lectores que quedan
			escritoresEnEspera++;	//si no puede escribir se quedará en espera hasta que sea su turno, por tanto incrementamos el número de escritores que hay esperando
			wait();
			escritoresEnEspera--;	//ya no está esperando
		}
		escritor = true;	//ahora hay un escritor
		
		System.out.println("Escritor " + numHilo + " va a empezar a escribir");
	}

	public synchronized void salirEscribir(int numHilo) {
		System.out.println("Escritor " + numHilo + " ha terminado de escribir");
		
		escritor = false;	//el escritor ha terminado y notifica (para que se peda volver a empezar a leer)
		notifyAll();
		
	}
	
}
