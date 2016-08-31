package ejercicio4;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;


public class HiloAux extends Thread{

	private AtomicInteger numMsg; // El número de mensaje
	private String msg; // El mensaje a defundir
	private ClientInterface clientInterface; // Cliente al que se le va a mandar el mensaje
	
	public HiloAux(AtomicInteger numMsg, String msg, ClientInterface clientInterface) {
		
		this.numMsg = numMsg;
		this.msg = msg;
		this.clientInterface = clientInterface;
		
	}

	public void run(){
		try {
			this.clientInterface.callback(this.numMsg, this.msg); // Ejecutamos la función que envia el mensaje al cliente para que lo guarde e imprima
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
}
