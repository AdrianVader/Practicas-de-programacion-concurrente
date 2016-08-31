package ejercicio4;

import java.util.concurrent.ConcurrentLinkedQueue;


public class HiloEscribir extends Thread {
	

	private ConcurrentLinkedQueue<String> listos; // Lista de mensajes ordenados que se quieren imprimir por pantalla
	
	
	public HiloEscribir(ConcurrentLinkedQueue<String> listos){
		this.listos = listos;
	}
	
	public void run(){

		String msg;
		System.out.println("Welcome !"); // Damos la bienvenida
		while(true){ 
			while(!this.listos.isEmpty()){ // Siempre que no esté vacía la cola, imprimiremos los mensajes, borrándolos de la misma 
				msg = this.listos.poll();
				if(msg != null)
					System.out.println(msg);
			}
		}
	}
	
}
