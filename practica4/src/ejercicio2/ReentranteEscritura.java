package ejercicio2;

import java.util.HashMap;



public class ReentranteEscritura implements MonitorArbitraje {

	private int numLectores = 0;
	private boolean escritor = false;
	private int escritoresEnEspera = 0;
	private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(); //HashMap que nos indica para cada hilo cu�ntas veces ha entrado a leer o a escribir.

	public synchronized void entrarLeer(int numHilo) throws InterruptedException {
		
		if(!tieneAcceso(numHilo)){ // Si ya tiene acceso a lectura no espera.
			while (hayEscritores()) // Mientras se est� escribiendo o haya alg�n escritor esperando su turno espera
				wait();
		}
		
		//Incrementamos el n�mero de veces que ha entrado a leer
		map.put(numHilo, map.get(numHilo)+1);
		numLectores++; // Ahora hay un lector m�s
		
		System.out.println("Lector " + numHilo + " va a empezar a leer " + map.get(numHilo) + " veces");
		
	}

	public synchronized void salirLeer(int numHilo) {
		
		System.out.println("Lector " + numHilo + " ha salido de leer");
		
		numLectores--; // Ahora hay un lector menos
		
		// Decrementamos el n�mero de veces que ha entrado a leer
		map.put(numHilo, map.get(numHilo)-1);
		
		if(numLectores <= 0) // Si era el �ltimo lector que quedaba (y ya no queda ninguno m�s) notificar para que los escritores puedan escribir
			notifyAll();
		
	}

	public synchronized void entrarEscribir(int numHilo) throws InterruptedException {
		
		if(!tieneAcceso(numHilo)){ // Si ya se le ha concedido el acceso a escritura(reentrante) no espera
			while(hayLectores() || escritor){ // Espera a que terminen los lectores que quedan o escritores.
				escritoresEnEspera++; // Si no puede escribir se quedar� en espera hasta que sea su turno, por tanto incrementamos el n�mero de escritores que hay esperando
				wait();
				escritoresEnEspera--; // Ya no est� esperando
			}
		}

		escritor = true;	// Ahora hay un escritor
			
		// Incrementamos el n�mero de veces que ha entrado a escribir
		map.put(numHilo, map.get(numHilo)+1);
		
		System.out.println("Escritor " + numHilo + " va a empezar a escribir");
	}

	public synchronized void salirEscribir(int numHilo) {
		
		System.out.println("Escritor " + numHilo + " ha terminado de escribir");
		
		map.put(numHilo, map.get(numHilo)-1);
		
		if(!tieneAcceso(numHilo)){ // Ha terminado de salir de todas sus escrituras
			escritor = false; // El escritor ha terminado y notifica (para que se peda volver a empezar a leer)
			notifyAll();
		}
	}

	
	//Si no est� en el HashMap (a�n nunca ha sido su turno) o su contador est� a cero (no ha entrado ninguna vez a leer en este turno)
	private boolean tieneAcceso(int numHilo){
		
		//Si no est� en el HashMap es porque no ha entrado nunca a leer, y lo inicializamos a cero (aun no ha le�do)
		if(!map.containsKey(numHilo))
			map.put(numHilo, 0);
		
		return (map.get(numHilo) > 0); // Devolvemos true si ya ha entrado
		
	}
	
	private boolean hayEscritores(){
		
		return (escritor || escritoresEnEspera > 0); // Devolvemos true en caso de que ya haya un escritor en espera o en ejecuci�n 
		
	}
	
	private boolean hayLectores(){
		
		return numLectores > 0; // Devolvemos true en el caso de que tengamos alg�n lector ejecutandose
		
	}
	
}
