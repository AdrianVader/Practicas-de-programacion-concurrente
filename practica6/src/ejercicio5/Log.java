package ejercicio5;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class Log extends Thread implements ClientInterface, Serializable {

	
	private ConcurrentLinkedQueue<String> pendientes; // Cola de mensajes pendientes, desordenados
	private ConcurrentLinkedQueue<String> listos; // Cola de mensajes ordenados y listos para imprimir
	private AtomicInteger cont; // Contador del siguiente mensaje a recibir(esperado)
	private AtomicInteger contListos; // Contador del último mensaje enviado a la cola de listos
	private Registry registry; // 
	
	
	public Log(Registry registry){

		this.pendientes = new ConcurrentLinkedQueue<String>();
		this.listos = new ConcurrentLinkedQueue<String>();
		this.cont = new AtomicInteger(-1); // Pondremos un valor centinela para saber cuál es el primer mensaje que debemos esperar
		this.contListos = new AtomicInteger(-1);
		this.registry = registry;
		
	}
	
public void callback(AtomicInteger numMsg, String msg){
    	
		if(this.cont.get() == -1) // Caso en el que acabamos de conectarnos
			this.cont.set(numMsg.get()); // Esperamos el actual, el primero que nos pasan
		
		AtomicInteger pos = new AtomicInteger(this.cont.get()); // Creamos una variable local para no usar la del cliente, ya que se va a acceder concurrentemente a ella 
		
		if(pos.get() == numMsg.get()){ // Es el mensaje que esperabamos
			this.listos.add(numMsg.get() + ": " + msg); 
			this.contListos.set(numMsg.get());
			pos.incrementAndGet();
			while(this.pendientes.contains(pos.get() + ": " + msg)){ // Buscamos en la cola de pendientes(retenidos) por si lla nos llegó
				this.pendientes.remove(pos.get() + ": " + msg);
				this.listos.add(pos.get() + ": " + msg);
				this.contListos.incrementAndGet();
				pos.incrementAndGet();
			}
		}
		else{
			this.pendientes.add(numMsg + ": " + msg);
			while(this.pendientes.contains(pos.get() + ": " + msg)){ // Debido a la concurrencia, nos aseguramos de que el que buscamos no está ya en los pendientes
				this.pendientes.remove(pos.get() + ": " + msg);
				this.listos.add(pos.get() + ": " + msg);
				this.contListos.incrementAndGet();
				pos.incrementAndGet();
			}
		}
		
		this.cont.set(pos.get()); // Actualizamos el contador del mensaje esperado
		
		if(this.contListos.get() >= this.cont.get()) // Comprobamos que esperamos un número mayor que el que hemos imprimido
			this.cont.incrementAndGet();
		
    }
    
    public void run(){
		
    	
		try {
        	
            ServerInterface stub = (ServerInterface) this.registry.lookup("Server"); // Asociamos el server a un stub

            ClientInterface stubClient = (ClientInterface) UnicastRemoteObject.exportObject(this, 0); // Guardamos(exportamos) el stub del log
            
            
            stub.inscribirse(stubClient); // Lo inscribimos para que le lleguen los mensajes
            
            new HiloGuardar(((Log) this).listos).start(); // Iniciamos el hilo que escribirá en un fichero los mensajes 
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
		
	}
    
}
