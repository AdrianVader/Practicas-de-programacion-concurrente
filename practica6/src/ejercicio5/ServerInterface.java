package ejercicio5;



import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void inscribirse(ClientInterface client) throws RemoteException;
    public void difundir(String msg) throws RemoteException;
}
