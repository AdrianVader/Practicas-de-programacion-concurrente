package ejercicio4;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;


public class HiloGuardar extends Thread {

	
	private ConcurrentLinkedQueue<String> listos; // Cola que almacena los mensajes, ordenados, que hay que escribir en el fichero 
	private PrintWriter out; // Variable para escribir en el fichero
	
	
	public HiloGuardar(ConcurrentLinkedQueue<String> listos){
		this.listos = listos;
		try {
	    	this.out = new PrintWriter(new FileWriter("output.txt")); // Creamos el fichero
	    } catch (IOException e1) {}
	}
	
	public void run(){
		
		String msg = "";
		while(true){
			while(!this.listos.isEmpty()){ // Siempre que no esté vacía la cola, meteremos los mensajes al fichero 
				msg = this.listos.poll();
				if(msg != null)
					this.out.println(msg);
			}
			
			this.out.flush();
		}
	}
	
}