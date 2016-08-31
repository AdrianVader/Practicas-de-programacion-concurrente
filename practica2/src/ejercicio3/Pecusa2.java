package ejercicio3;

// PECUSA2: = el Primero que Entra Cierra, el último que Sale Abre (versión 2)
public class Pecusa2 {

	int contador = 0;
	java.util.concurrent.Semaphore mi_semaforo;

	// Suponemos que sem es un Semáforo inicializado a 1
	public Pecusa2(java.util.concurrent.Semaphore sem){
		mi_semaforo = sem;
	}

	public synchronized void cerrar_al_otro_genero_si_primero() {
		contador++;
		if (contador == 1)
			try{mi_semaforo.acquire();} catch(Exception e) {}
	}

	public synchronized void abrir_al_otro_genero_si_ultimo() {
		contador--;
		if (contador == 0) mi_semaforo.release();
	}
}