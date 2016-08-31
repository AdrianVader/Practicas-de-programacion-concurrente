package parte2;


public class Secuenciador {

	private int sec = 0;
	
	public synchronized int ticket(){
		return ++sec;
	}
	
}
