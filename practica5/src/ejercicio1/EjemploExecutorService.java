package ejercicio1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class EjemploExecutorService {

  // El main:

	public static void main(String[] argv) {
		
	    System.out.println("Numero de procesadores disponibles: " + Runtime.getRuntime().availableProcessors()); // Imprimimos el número de procesadores del que disponemos.
	
	    final int numTareas = 1000;
	    final int numMaxHilos = 50;
	    
	    PrintWriter out = null; // Creamos un fichero de salida para sacar varios archivos y compararlos
	    try {
	    	out = new PrintWriter(new FileWriter("output.txt"));
	    } catch (IOException e1) {}
	
	    System.out.println("Numero de hilos" + '\t' + '\t' + "tiempo en ms");
	    
	    // COMENZAR BUCLE. Para un número de hilos (numHilos) entre 1 y numMaxHilos:
	    
	    for(int numHilos = 1; numHilos <= numMaxHilos; numHilos++){ // Recorremos todos los valores de los hilos
	    	
	    	final long startTime = System.nanoTime(); // Empezamos a contar
		
		    // Utilice un método factoría para crear un ThreadPool (implementación
		    // de la interfaz ExecutorService) con el número de hilos requerido.
		    ExecutorService pool = Executors.newScheduledThreadPool(numHilos); // Creamos un nuevo pool, con el número de hilos por el que vayamos en el bucle, así comprobaremos la eficiencia de cada uno
		    
		    // COMENZAR BUCLE. Para un número de tareas entre 1 y numTareas:
		    	// Crear un nuevo objeto de la clase TareaDeCalculo (con p.e. maxCont == 1000000) <- Hemos rebajado el contador para que tarde menos
		    	// Presente esta tarea al ExecutorService para su ejecución
		    // TERMINAR BUCLE
		    for(int tareas = 1; tareas < numTareas; tareas++) // Ejecutamos con el pool numTareas Tareas de cálculo
		    	pool.execute(new TareaDeCalculo(10000));
		    	
		      // Intente cerrar el ExecutorService de manera ordenada, es decir, dejando las
		      // tareas activas terminar (¡como cuando se grita &quot;<em>last orders</em>&quot;
		      // en un pub inglés!).
		      pool.shutdown();
		    
		      // Espere que se termine el ExecutorService (o bien que pasen unos segundos)
		     try {
		    	  if(!pool.awaitTermination(1, TimeUnit.SECONDS)){ // Si pasa 1 ms cerraremos bruscamente el pool
				      // Aunque no es necesario en este ejemplo ya que no habrá tareas que tarden demasiado
				      // en terminarse, ahora intente cerrar el ExecutorService de manera brusca,
				      // es decir, interrumpiendo tareas activas (¡como cuando se grita &quot;<em>time</em>&quot;
				      // en un pub inglés!).
		    		  System.out.println("Cerramos el pool bruscamente");
		    		  pool.shutdownNow();
		    	  }
		    	  } catch (InterruptedException e) {}

		
		      System.out.println(numHilos + "\t" + "\t" + "\t" + (System.nanoTime() - startTime)/1000000);

			  out.println(numHilos + "\t" + "\t" + "\t" + (System.nanoTime() - startTime)/1000000); // Lo mostramos en milisegundos
			    
		    // TERMINAR BUCLE.
	    }

	    out.close();
	    
	}

	
 }