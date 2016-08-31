package ejercicio2;



import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    void callback(String msg) throws RemoteException;
	
}
