package ejercicio4;

import java.util.HashMap;



public class ReentranteTotal implements MonitorArbitraje {

	private int numLectores = 0;
	private boolean escritor = false;
	private int escritoresEnEspera = 0;
	private HashMap<Integer, Integer> mapL = new HashMap<Integer, Integer>();	//HashMap que nos indica para cada hilo cuántas veces ha entrado a leer.
	private HashMap<Integer, Integer> mapE = new HashMap<Integer, Integer>();	//HashMap que nos indica para cada hilo cuántas veces ha entrado a escribir.
	
	public synchronized void entrarLeer(int numHilo) throws InterruptedException {
		
		if(!tieneAcceso(mapE, numHilo)){ // Si es un escritor y tiene acceso, no hay problema, no espera
			if(!tieneAcceso(mapL, numHilo)){ // Si ya tiene acceso a lectura no espera.
				while (hayEscritores()) // Mientras se esté escribiendo o haya algún escritor esperando su turno espera
					wait();
			}
		}
		
		if(!mapL.containsKey(numHilo)) // <- Hay que inicializarlo, por si no se ha usado todavía
			mapL.put(numHilo, 0);
		
		//Incrementamos el número de veces que ha entrado a leer
		mapL.put(numHilo, mapL.get(numHilo)+1);
		numLectores++; // Ahora hay un lector más
		
		System.out.println("Lector " + numHilo + " va a empezar a leer " + mapL.get(numHilo) + " veces");
		
	}

public synchronized void salirLeer(int numHilo) {
		
		System.out.println("Lector " + numHilo + " ha salido de leer");
		
		numLectores--;	//ahora hay un lector menos
		
		// Decrementamos el número de veces que ha entrado a leer
		mapL.put(numHilo, mapL.get(numHilo)-1);
		
		if(numLectores <= 0) // Si era el último lector que quedaba (y ya no queda ninguno más) notificar para que los escritores puedan escribir
			notifyAll();
		
	}

	public synchronized void entrarEscribir(int numHilo) throws InterruptedException {
		
		if(!tieneAcceso(mapE, numHilo)){
			while(hayLectores() || escritor){ // Espera a que terminen los lectores que quedan
					escritoresEnEspera++;
					wait();
					escritoresEnEspera--;
			}
		}
		
		escritor = true;	//Ahora hay un escritor
		
		// Incrementamos el número de veces que ha entrado a escribir
		mapE.put(numHilo, mapE.get(numHilo)+1);
		
		System.out.println("Escritor " + numHilo + " va a empezar a escribir");

	}

	public synchronized void salirEscribir(int numHilo) {
		
		System.out.println("Escritor " + numHilo + " ha terminado de escribir");
		
		mapE.put(numHilo, mapE.get(numHilo)-1);
		
		if(!tieneAcceso(mapE, numHilo)){ // Ha terminado de salir de todas sus escrituras
			escritor = false; // El escritor ha terminado y notifica (para que se peda volver a empezar a leer)
			notifyAll();
		}
	}

	
	//Si no está en el HashMap (aún nunca ha sido su turno) o su contador está a cero (no ha entrado ninguna vez a leer en este turno)
	private boolean tieneAcceso(HashMap<Integer,Integer> map, int numHilo){
		
		//Si no está en el HashMap es porque no ha entrado nunca a leer, y lo inicializamos a cero (aun no ha leído)
		if(!map.containsKey(numHilo))
			map.put(numHilo, 0);
		
		return (map.get(numHilo) > 0); // Devolvemos true si ya ha entrado
		
	}
	
	private boolean hayEscritores(){
		
		return (escritor || escritoresEnEspera > 0); // Devolvemos true en caso de que ya haya un escritor en espera o en ejecución 
		
	}
	
	private boolean hayLectores(){
		
		return numLectores > 0; // Devolvemos true en el caso de que tengamos algún lector ejecutandose
		
	}
	
}

