package ejercicio5;

import java.rmi.RemoteException;


public class HiloDifundir extends Thread {

	private String msg; // Mensaje que se quiere difundir
	private ServerInterface stub; // Servidor al que se va a mandar el mensaje
	
	public HiloDifundir(String msg, ServerInterface stub){
		
		this.msg = msg;
		this.stub = stub;
		
	}
	
	public void run(){
		try {
			this.stub.difundir(this.msg); // Llamamos a la función del servidor que enviará al los clientes el mensaje   
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
}
