package ejercicio3;
import java.util.Random;

class Persona2 extends Thread {

	protected static final int MAX = 4 -1;

	// Random Number Generator
	static Random rnd = new Random();

	// Número que sirve de indentificador del objeto
	final int mi_numero;
	final boolean mujer;
	
	static java.util.concurrent.Semaphore semVar;
	static java.util.concurrent.Semaphore semaforo;

	// PECUSA que controla el acceso múltiple a la base de datos
	static Pecusa2 disponibilidadHombres;
	static Pecusa2 disponibilidadMujeres;
	
	// Número de chicos que hay en el baño
	static int CHICOS = 0;

	public static int getCHICOS() {
		return CHICOS;
	}
	
	// Número de chicas que hay en el baño
	static int CHICAS = 0;

	public static int getCHICAS() {
		return CHICAS;
	}

	// Crea una persona
	Persona2(int num, boolean mujer, java.util.concurrent.Semaphore sem){
		mi_numero = num;
		this.mujer = mujer;
		if (this.mujer && disponibilidadMujeres == null) {
			disponibilidadMujeres = new Pecusa2(sem);
		}
		if (!this.mujer && disponibilidadHombres == null) {
			disponibilidadHombres = new Pecusa2(sem);
		}
		if(semVar==null){
			semVar=new java.util.concurrent.Semaphore(1);
		}
		if(semaforo==null){
			semaforo=new java.util.concurrent.Semaphore(1);
		}
	}

	// Función para trabajar durante un tiempo
	protected void trabaja(){
		try {
			sleep(rnd.nextInt(100));
		} catch (InterruptedException e) {
			System.err.println("Interrupted while waiting");
		}
	}
	
	// Función para utilizar el servicio durante un tiempo
	protected void utilizarServicios(){
		try {
			sleep(rnd.nextInt(100));
		} catch (InterruptedException e) {
			System.err.println("Interrupted while using the toilet");
		}
	}

	public void run(){
		if(this.mujer){	// Código que ejecuta una mujer
			while(true){
				
				trabaja();
				
					// Comprueba la cantidad de chicas que hay en el baño
				try {semaforo.acquire();} catch (InterruptedException e1) {}
				
				// Ahora solo podrán entrar mujeres
				disponibilidadMujeres.cerrar_al_otro_genero_si_primero();

				System.out.println("Chica " + mi_numero + " entrando.");
				try {semVar.acquire();} catch (InterruptedException e) {}
					CHICAS++;
				semVar.release();
				
				
				utilizarServicios();
				
				try {semVar.acquire();} catch (InterruptedException e) {}
					CHICAS--;
				semVar.release();
				System.out.println("Chica " + mi_numero + " saliendo.");
				
				// Si es la última persona en salir del baño, permite que entre cualquier género
				disponibilidadMujeres.abrir_al_otro_genero_si_ultimo();
				semaforo.release();
				
			}
		}
		else{	// Código que ejecuta un hombre
			while(true){

					trabaja();
				
				// Comprueba la cantidad de chicas que hay en el baño
				try {semaforo.acquire();} catch (InterruptedException e1) {}

				// Ahora solo podrán entrar hombres
				disponibilidadHombres.cerrar_al_otro_genero_si_primero();

				System.out.println("Chico " + mi_numero + " entrando.");
				try {semVar.acquire();} catch (InterruptedException e) {}
					CHICOS++;
				semVar.release();
				
				utilizarServicios();
				
				try {semVar.acquire();} catch (InterruptedException e) {}
					CHICOS--;
				semVar.release();
				System.out.println("Chico " + mi_numero + " saliendo.");
				
				// Si es la última persona en salir del baño, permite que entre cualquier género
				disponibilidadHombres.abrir_al_otro_genero_si_ultimo();
				semaforo.release();
				
			}
		}
	}
}