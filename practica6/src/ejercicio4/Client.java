package ejercicio4;




import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Client extends Thread implements ClientInterface, Serializable {
	
	private ConcurrentLinkedQueue<String> pendientes; // Cola de mensajes pendientes, desordenados
	private ConcurrentLinkedQueue<String> listos; // Cola de mensajes ordenados y listos para imprimir
	private AtomicInteger cont; // Contador del siguiente mensaje a recibir(esperado)
	private AtomicInteger contListos; // Contador del último mensaje enviado a la cola de listos
	
	
	public Client(){

		this.pendientes = new ConcurrentLinkedQueue<String>();
		this.listos = new ConcurrentLinkedQueue<String>();
		this.cont = new AtomicInteger(-1); // Pondremos un valor centinela para saber cuál es el primer mensaje que debemos esperar
		this.contListos = new AtomicInteger(-1);
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

	public static void main(String[] args) {


        String host = (args.length < 1) ? null : args[0]; // Si se quiere ejecutar en máquinas diferentes utilizaremos el host que nos pasen por argumento
        try {
        	
            Registry registry = LocateRegistry.getRegistry(host); // Cogemos el registro
            ServerInterface stub = (ServerInterface) registry.lookup("Server"); // Asociamos el server a un stub
            
            ClientInterface c = new Client(); // Creamos un cliente
            ClientInterface stubClient = (ClientInterface) UnicastRemoteObject.exportObject(c, 0); // Guardamos(exportamos) el stub del cliente, para acceder 
            
            
            stub.inscribirse(stubClient); // Inscribimos el cliente
            
        	String msg;
        	
        	Client.sleep(2000); // Hacemos una espera para poder ejecutar a la vez varios clientes, a la hora de probarlo
        	
        	new HiloEscribir(((Client) c).listos).start(); // Creamos y ejecutamos el hilo del cliente que se encargará de imprimir por pantalla los mensajes en orden
            
            for(int i = 0; i < 100;i++){ // Creamos los mensajes en diferentes espacios de tiempo
            	
				Client.sleep((long) (Math.random()*10));
				
            	msg = "Hello world!"; // Mensaje
            	
                new HiloDifundir(msg, stub).start(); // Creamos el hilo encargado de difundir el mensaje a todos los clientes, por medio del stub del servidor
            	
                
            }
            

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    
    
}
