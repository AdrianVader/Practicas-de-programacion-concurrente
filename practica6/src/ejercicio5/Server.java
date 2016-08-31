package ejercicio5;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class Server implements ServerInterface{

	private CopyOnWriteArrayList<ClientInterface> clientList; // Lista de interfaces de Clientes que realiza copias de la lista para recorrerla sin ser modificada
	private AtomicInteger numMsg; // Número de mensaje por el que se va en el servidor
	
    public Server() {
    	
    	this.clientList = new CopyOnWriteArrayList<ClientInterface>();
    	this.numMsg = new AtomicInteger(0);
    	
    }
    
    public void inscribirse(ClientInterface client) throws RemoteException{
    	
    	this.clientList.add(client); // Añadimos el cliente
    	System.out.println("Hay un nuevo cliente.");
    }
    
    public void difundir(String msg) throws RemoteException{

    	AtomicInteger num = new AtomicInteger(1);
		num.set(this.numMsg.incrementAndGet()); // Actualizamos el siguiente número de mensaje
		
    	Iterator it = this.clientList.iterator();
    	while(it.hasNext()){ // Recorremos los clientes que se han inscrito hasta este momento
    		new HiloAux(num, msg, (ClientInterface) it.next()).start(); // Creamos hilos para difundir el mensaje a cada cliente
    	}
        
    }
    
    public static void main(String args[]) {

        try {
        	
        	Utils.setCodeBase(ServerInterface.class);
            Server obj = new Server(); // Creamos el servidor
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(obj, 0); // Exportamos el servidor

            Registry registry = LocateRegistry.getRegistry(); // Cogemos el registro
            registry.bind("Server", stub); // Guardamos el stub del servidor como "Server", para poder hacer lookUp y recuperarlo en los clientes

            
            new Log(registry).start(); // Iniciamos el log, donde se registrarán todos los mensajes enviados
            
            
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        
    }
}
