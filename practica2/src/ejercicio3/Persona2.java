package ejercicio3;
import java.util.Random;

class Persona2 extends Thread {

	protected static final int MAX = 4 -1;

	// Random Number Generator
	static Random rnd = new Random();

	// N�mero que sirve de indentificador del objeto
	final int mi_numero;
	final boolean mujer;
	
	static java.util.concurrent.Semaphore semVar;
	static java.util.concurrent.Semaphore semaforo;

	// PECUSA que controla el acceso m�ltiple a la base de datos
	static Pecusa2 disponibilidadHombres;
	static Pecusa2 disponibilidadMujeres;
	
	// N�mero de chicos que hay en el ba�o
	static int CHICOS = 0;

	public static int getCHICOS() {
		return CHICOS;
	}
	
	// N�mero de chicas que hay en el ba�o
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

	// Funci�n para trabajar durante un tiempo
	protected void trabaja(){
		try {
			sleep(rnd.nextInt(100));
		} catch (InterruptedException e) {
			System.err.println("Interrupted while waiting");
		}
	}
	
	// Funci�n para utilizar el servicio durante un tiempo
	protected void utilizarServicios(){
		try {
			sleep(rnd.nextInt(100));
		} catch (InterruptedException e) {
			System.err.println("Interrupted while using the toilet");
		}
	}

	public void run(){
		if(this.mujer){	// C�digo que ejecuta una mujer
			while(true){
				
				trabaja();
				
					// Comprueba la cantidad de chicas que hay en el ba�o
				try {semaforo.acquire();} catch (InterruptedException e1) {}
				
				// Ahora solo podr�n entrar mujeres
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
				
				// Si es la �ltima persona en salir del ba�o, permite que entre cualquier g�nero
				disponibilidadMujeres.abrir_al_otro_genero_si_ultimo();
				semaforo.release();
				
			}
		}
		else{	// C�digo que ejecuta un hombre
			while(true){

					trabaja();
				
				// Comprueba la cantidad de chicas que hay en el ba�o
				try {semaforo.acquire();} catch (InterruptedException e1) {}

				// Ahora solo podr�n entrar hombres
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
				
				// Si es la �ltima persona en salir del ba�o, permite que entre cualquier g�nero
				disponibilidadHombres.abrir_al_otro_genero_si_ultimo();
				semaforo.release();
				
			}
		}
	}
}