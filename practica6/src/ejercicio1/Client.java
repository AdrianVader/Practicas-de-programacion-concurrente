package ejercicio1;




import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client implements ClientInterface, Serializable {
	
    private Client() {
    }

	public void callback(String msg){ // Imprimimos por pantalla el mensaje
    	
    	System.out.println(msg);
    }

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
        	
            Registry registry = LocateRegistry.getRegistry(host); // Nos conectamos al host
            ServerInterface stub = (ServerInterface) registry.lookup("Server");
            
            ClientInterface c = new Client(); // Creamos un cliente y lo exportamos
            ClientInterface stubClient = (ClientInterface) UnicastRemoteObject.exportObject(c, 0);
            
            
            stub.inscribirse(stubClient); // Inscribimos al cliente
            
        	String msg;
        	Scanner sc = new Scanner(System.in);
        	
            
            
            while(true){
            	msg = sc.nextLine(); // Pedimos una nueva linea para enviarla como mensaje
                stub.difundir(msg);
            }
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
