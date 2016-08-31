package ejercicio3;




import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client extends Thread implements ClientInterface, Serializable {
	
    
	
	private Registry registry;
	
	
	public Client(Registry registry){
		this.registry = registry;
	}
	

	public void callback(String msg){ // Imprimimos por pantalla el mensaje
    	
    	System.out.println(msg);
    }

    public void run() {

        try {
        	
            ServerInterface stub = (ServerInterface) this.registry.lookup("Server");
            
            ClientInterface stubClient = (ClientInterface) UnicastRemoteObject.exportObject(this, 0);
            
            
            stub.inscribirse(stubClient); // Inscribimos al cliente
            
        	String msg;
        	
            
        	for(int i = 0; i < 100;i++){
            	
				Client.sleep((long) (Math.random()*10));
				
            	msg = "Hello world!";
            	
                stub.difundir(msg);
                
            }
            
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        } 
    }
    
}
