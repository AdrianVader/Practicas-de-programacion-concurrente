package ejercicio3;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class ReentranteLecturaEscritura implements MonitorArbitraje {

	private int numLectores = 0;
	private boolean escritor = false;
	private int escritoresEnEspera = 0;
	private HashMap<Integer, Integer> mapL = new HashMap<Integer, Integer>(); // HashMap que nos indica para cada hilo cuántas veces ha entrado a leer.
	private HashMap<Integer, Integer> mapE = new HashMap<Integer, Integer>(); // HashMap que nos indica para cada hilo cuántas veces ha entrado a escribir.
	
	public synchronized void entrarLeer(int numHilo) throws InterruptedException {
		
		if(!tieneAcceso(mapL, numHilo)){ // Si ya tiene acceso a lectura no espera.
			while (hayEscritores()) // Mientras se esté escribiendo o haya algún escritor esperando su turno espera
				wait();
		}
		
		//Incrementamos el número de veces que ha entrado a leer
		mapL.put(numHilo, mapL.get(numHilo)+1);
		numLectores++; // Ahora hay un lector más
		
		System.out.println("Lector " + numHilo + " va a empezar a leer " + mapL.get(numHilo) + " veces");
		
	}

	public synchronized void salirLeer(int numHilo) {
		
		System.out.println("Lector " + numHilo + " ha salido de leer");
		
		numLectores--;	// Ahora hay un lector menos
		
		// Decrementamos el número de veces que ha entrado a leer
		mapL.put(numHilo, mapL.get(numHilo)-1);
		
		if(unicoLector()) // Si solo queda el último lector se lo notificamos para que pueda escribir
			notifyAll();
		
	}

	public synchronized void entrarEscribir(int numHilo) throws InterruptedException {
		
		if(!tieneAcceso(mapE, numHilo)){ // Si ya se le ha concedido el acceso a escritura(reentrante) no espera
			while (!unicoLector()){ // Si no es el último lector esperará a serlo y no permitirá que otros esperen 
				if(hayEscritoresEnEspera())
					throw new InterruptedException(); // Lanzamos una excepción para controlar el interbloqueo
				escritoresEnEspera++;
				wait();
				escritoresEnEspera--;
			}
		}
		
		escritor = true; // Ahora hay un escritor
						
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
	
	
	public boolean unicoLector(){
		Iterator<Entry<Integer, Integer>> it = mapL.entrySet().iterator();
		int numero = 0;
		
		while(it.hasNext()){
			Map.Entry<Integer, Integer> e = (Map.Entry<Integer, Integer>)it.next();
			if((int)e.getValue() >= 1)
				numero++;
		}
		
		if (numero == 1)
			return true;
		return false;
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
	
	private boolean hayEscritoresEnEspera(){
		
		return escritoresEnEspera > 0;
		
	}
	
}

