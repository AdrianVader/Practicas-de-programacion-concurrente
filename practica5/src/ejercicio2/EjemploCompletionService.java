package ejercicio2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
 
public class EjemploCompletionService {
 
	
	final static int numTareas = 10;
    final static int numHilos = 10;
    static ArrayList<TareaLarga> tareas = new ArrayList<TareaLarga>();
	
    // Defina un método estático sin argumentos llamado crearListaTareas
    // que cree una lista de, por ejemplo, diez TareasLargas.
	
	public static void crearListaTareas(){
		for(int i = 0; i< numTareas; i++)
			tareas.add(new TareaLarga());	//Crea numTareas (10 en este caso) TareasLargas nuevas y las guarda en la lista "tareas"
	}


    // El main:
	public static void main(String[] argv) {
 
      // Utilice un método factoría para crear un ThreadPool (implementación
      // de la interfaz ExecutorService) con el numero de hilos requerido:
      // por ejemplo, diez.
		
		ExecutorService pool = Executors.newScheduledThreadPool(numHilos);	//Creamos el ThreadPool

      // Cree un nuevo CompletionService para tareas que devuelvan un String,
      // pasándole el ExecutorService creado en la instrucción anterior
		ExecutorCompletionService<ExecutorService> executor = new ExecutorCompletionService<ExecutorService>(pool);	//Creamos el CompletionService pasándole como argumento el pool creado antes
  
      // Cree una lista de TareaLarga con el método crearListaTareas
		crearListaTareas();	//Crea las TareasLargas y las guarda en el ArrayList

      // COMENZAR BUCLE (DE TIPO FOR-EACH). Para cada tarea de la lista
          // Presente esta tarea al CompletionService para su ejecución
      // TERMINAR BUCLE.
		Iterator<TareaLarga> it = tareas.iterator();
		while(it.hasNext()){
			executor.submit((Callable) it.next());	//Presentamos cada tarea para su ejecución
		}
		System.out.println();

      // COMENZAR BUCLE. Para un número de veces = el tamaño de la lista de tareas
          // Pida una tarea completada al CompletionService
          //   (espere si no ha terminado ninguna tarea todavía)
          // Imprima el resultado de la tarea en la salida estándar.
      // TERMINAR BUCLE.
		
		for(int i = 0; i < tareas.size();i++){
			try {
				System.out.println(executor.take());	//Mostramos los resultados
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

      // Cierre el ExecutionService
		pool.shutdown();
	}
}