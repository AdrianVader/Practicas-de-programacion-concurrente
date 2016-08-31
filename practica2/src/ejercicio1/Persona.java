package ejercicio1;

import java.util.Random;

class Persona extends Thread {

	protected static final int MAX = 4 -1; //El máximo serán 4 personas, por tanto, para nosotros 3 porque contamos el 0

	// Random Number Generator
	static Random rnd = new Random();

	// Número que sirve de indentificador del objeto
	final int mi_numero;
	final boolean mujer;
	
	static Semaphore semVar; //Semáforo para proteger las variables CHICAS y CHICOS
	static Semaphore semaforo; //Semáforo que se inicializa a MAX para controlar el número de personas del servicio

	// PECUSA que controla el acceso múltiple a la base de datos
	static Pecusa disponibilidadHombres; //Pecusa que compite por un semáforo para permitir solo un género dentro de los servicios
	static Pecusa disponibilidadMujeres; //Pecusa que compite por un semáforo para permitir solo un género dentro de los servicios
	
	// Número de chicos que hay en el baño
	static int CHICOS = 0; //Cuenta el numero actual de hombres en el servicio

	public static int getCHICOS() {
		return CHICOS;
	}
	
	// Número de chicas que hay en el baño
	static int CHICAS = 0; //Cuenta el numero actual de mujeres en el servicio

	public static int getCHICAS() {
		return CHICAS;
	}

	// Crea una persona
	Persona(int num, boolean mujer, Semaphore sem){ //Constructora que inicializa lo necesario
		mi_numero = num;
		this.mujer = mujer;
		if (this.mujer && disponibilidadMujeres == null) {
			disponibilidadMujeres = new Pecusa(sem);
		}
		if (!this.mujer && disponibilidadHombres == null) {
			disponibilidadHombres = new Pecusa(sem);
		}
		if(semVar==null){
			semVar=new Semaphore(1);
		}
		if(semaforo==null){
			semaforo=new Semaphore(MAX);
		}
	}

	// Función para trabajar durante un tiempo
	protected void trabaja(){
		try {
			sleep(rnd.nextInt(100));
		} catch (InterruptedException e) {
			System.err.println("Interrupted while working");
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
				try {semaforo.P();} catch (InterruptedException e1) {}
				
				// Ahora solo podrán entrar mujeres
				disponibilidadMujeres.cerrar_al_otro_genero_si_primero();

				System.out.println("Chica " + mi_numero + " entrando.");
				try {semVar.P();} catch (InterruptedException e) {}
					CHICAS++;
				semVar.V();
				
				utilizarServicios();
				
				try {semVar.P();} catch (InterruptedException e) {}
					CHICAS--;
				semVar.V();
				System.out.println("Chica " + mi_numero + " saliendo.");
				
				// Si es la última persona en salir del baño, permite que entre cualquier género
				disponibilidadMujeres.abrir_al_otro_genero_si_ultimo();
				semaforo.V();
				
			}
		}
		else{	// Código que ejecuta un hombre
			while(true){

					trabaja();
				
				// Comprueba la cantidad de chicas que hay en el baño
				try {semaforo.P();} catch (InterruptedException e1) {}
				
				// Ahora solo podrán entrar hombres
				disponibilidadHombres.cerrar_al_otro_genero_si_primero();

				System.out.println("Chico " + mi_numero + " entrando.");
				try {semVar.P();} catch (InterruptedException e) {}
					CHICOS++;
				semVar.V();
				
				utilizarServicios();
				
				try {semVar.P();} catch (InterruptedException e) {}
					CHICOS--;
				semVar.V();
				System.out.println("Chico " + mi_numero + " saliendo.");
				
				// Si es la última persona en salir del baño, permite que entre cualquier género
				disponibilidadHombres.abrir_al_otro_genero_si_ultimo();
				semaforo.V();
				
			}
		}
	}
}