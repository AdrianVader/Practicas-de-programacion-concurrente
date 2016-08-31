package ejercicio2;



import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server implements ServerInterface{

	private ClientInterface[] clientList; // Lista de Clientes
	private final int N = 100; // Número máximo de clientes
	private int numActual; // Cantidad actual de clientes
	private int numMsg; // Número del mensaje
	
    public Server() {
    	
    	this.clientList = new ClientInterface[N];
    	this.numActual = 0;
    	this.numMsg = 1;
    	
    }
    
    public void inscribirse(ClientInterface client) throws RemoteException{
    	
    	this.clientList[this.numActual] = client; // Introducimos el nuevo cliente
    	this.numActual++;
    	System.out.println("Hay un nuevo cliente.");
    }
    
    public void difundir(String msg) throws RemoteException{

    	for(int i = 0; i < this.numActual;i++){ // Recorremos todos los clientes y llamamos a Callback, para enviarles el mensaje
    		this.clientList[i].callback(this.numMsg + ": " + msg);
    	}
    	this.numMsg++;
        
    }
    
    public static void main(String args[]) {

        try {
        	Utils.setCodeBase(ServerInterface.class);
            Server obj = new Server();
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(obj, 0); // Exportamos el server y guardamos el stub
            
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Server", stub);

            for(int i = 0; i < 5;i++)
            	new Client(registry).start();
            
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        
    }
}
