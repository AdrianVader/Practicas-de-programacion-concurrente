package parte2;


public class MonitorSinInanicionCat implements MonitorArbitraje {

	private int numLectores = 0;
	private boolean escritor = false;
	private int escritoresEnEspera = 0;
	private boolean turnoLectores = false;

	public synchronized void entrarLeer(int numHilo) throws InterruptedException {
		
		turnoLectores = true;	//se activa el turno de los lectores, así aunque tengan que esperar, no podrán sufrir inanición
		while (escritor || escritoresEnEspera > 0){	//mientras se esté escribiendo o haya algún escritor esperando su turno espera
			wait();
		}

		turnoLectores = false;	//ya no es su turno
		
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
		
		while (turnoLectores){	//es el turno de los lectores (puede haber alguno esperando a ser atendido y por tanto hay que dejarle paso)
			wait();
		}

		escritor = true;//ahora hay un escritor
				
		while(numLectores > 0){	// Espera a que terminen los que quedan
			escritoresEnEspera++;	//si no puede escribir se quedará en espera hasta que sea su turno, por tanto incrementamos el número de escritores que hay esperando
			wait();
			escritoresEnEspera--;	//ya no está esperando
		}
		
		System.out.println("Escritor " + numHilo + " va a empezar a escribir");
	}

	public synchronized void salirEscribir(int numHilo) {
		System.out.println("Escritor " + numHilo + " ha terminado de escribir");
		
		escritor = false;	//el escritor ha terminado y notifica (para que se peda volver a empezar a leer)
		notifyAll();
		
	}

}
