package ejercicio1;

import java.util.Random;

class Persona extends Thread {

	protected static final int MAX = 4 -1; //El m�ximo ser�n 4 personas, por tanto, para nosotros 3 porque contamos el 0

	// Random Number Generator
	static Random rnd = new Random();

	// N�mero que sirve de indentificador del objeto
	final int mi_numero;
	final boolean mujer;
	
	static Semaphore semVar; //Sem�foro para proteger las variables CHICAS y CHICOS
	static Semaphore semaforo; //Sem�foro que se inicializa a MAX para controlar el n�mero de personas del servicio

	// PECUSA que controla el acceso m�ltiple a la base de datos
	static Pecusa disponibilidadHombres; //Pecusa que compite por un sem�foro para permitir solo un g�nero dentro de los servicios
	static Pecusa disponibilidadMujeres; //Pecusa que compite por un sem�foro para permitir solo un g�nero dentro de los servicios
	
	// N�mero de chicos que hay en el ba�o
	static int CHICOS = 0; //Cuenta el numero actual de hombres en el servicio

	public static int getCHICOS() {
		return CHICOS;
	}
	
	// N�mero de chicas que hay en el ba�o
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

	// Funci�n para trabajar durante un tiempo
	protected void trabaja(){
		try {
			sleep(rnd.nextInt(100));
		} catch (InterruptedException e) {
			System.err.println("Interrupted while working");
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
				try {semaforo.P();} catch (InterruptedException e1) {}
				
				// Ahora solo podr�n entrar mujeres
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
				
				// Si es la �ltima persona en salir del ba�o, permite que entre cualquier g�nero
				disponibilidadMujeres.abrir_al_otro_genero_si_ultimo();
				semaforo.V();
				
			}
		}
		else{	// C�digo que ejecuta un hombre
			while(true){

					trabaja();
				
				// Comprueba la cantidad de chicas que hay en el ba�o
				try {semaforo.P();} catch (InterruptedException e1) {}
				
				// Ahora solo podr�n entrar hombres
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
				
				// Si es la �ltima persona en salir del ba�o, permite que entre cualquier g�nero
				disponibilidadHombres.abrir_al_otro_genero_si_ultimo();
				semaforo.V();
				
			}
		}
	}
}