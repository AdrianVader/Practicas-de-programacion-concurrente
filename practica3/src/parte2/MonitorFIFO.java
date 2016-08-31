package parte2;


public class MonitorFIFO implements MonitorArbitraje {

	private int numLectores = 0;
	private boolean escritor = false;
	private int turno = 1;
	private Secuenciador sec = new Secuenciador();

	public synchronized void entrarLeer(int numHilo) throws InterruptedException {
		
		int miTurno = sec.ticket();	//el lector pide turno (se incrementa el valor del secuenciador)
		
		while (escritor || miTurno > turno){	//mientras haya algún escritor escribiendo o no sea su turno espera
			wait();
		}
		turno++;	//se incrementa el turno real en el que está
		
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
		
		int miTurno = sec.ticket();	//el escritor pide turno (se incrementa el valor del secuenciador)
		
		while (numLectores > 0 || miTurno > turno){	//mientras haya algún lector leyendo o no sea su turno espera
			wait();
		}
		
		escritor = true;	//ahora hay un escritor
		
		turno++;	//se incrementa el turno real en el que está
		
		System.out.println("Escritor " + numHilo + " va a empezar a escribir");
	}

	public synchronized void salirEscribir(int numHilo) {
		System.out.println("Escritor " + numHilo + " ha terminado de escribir");
		
		escritor = false;	//el escritor ha terminado y notifica (para que se peda volver a empezar a leer)
		notifyAll();
	
	}
	
}
