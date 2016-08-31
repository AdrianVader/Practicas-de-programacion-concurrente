package ejercicio5;



import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;

public interface ClientInterface extends Remote {

    void callback(AtomicInteger numMsg, String msg) throws RemoteException;
	
}

