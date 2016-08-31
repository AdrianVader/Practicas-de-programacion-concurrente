package ejercicio1;


public class TareaDeCalculo implements Runnable{

	
	private int maxCont; // Marca hasta la que vamos a contar.
	private int cont; // Acumulador
	
	public TareaDeCalculo(int cont){
		this.maxCont = cont;
		this.cont = 0; // Lo inicializamos a 0.
	}
	
	@Override
	public void run() {
		while(this.maxCont > cont) // Contamos desde 0 hasta maxCont
			this.cont++;
	}

}
