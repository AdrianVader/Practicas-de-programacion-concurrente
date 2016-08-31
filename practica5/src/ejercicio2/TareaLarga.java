package ejercicio2;

import java.util.Random;
import java.util.concurrent.Callable;


public class TareaLarga implements Callable{	//Ahora implementamos TareaLarga como un callable
	@Override
	public String call() throws Exception {	//Sustituimos el método run() por call()
		try {
			Thread.sleep(Math.abs(new Random().nextLong() % 5000));	//Lo único que hace esta clase es esperar un tiempo largo aleatorio y...
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Thread.currentThread().toString();	//...por último imprimir su nombre
	}

	
}
