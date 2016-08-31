package ejercicio1;


public class ServiciosUnisex{

	// Semáforo que se utiliza para controlar el acceso a la base de datos
	static final Semaphore libre = new Semaphore(1);

		public static void main(String[] args){
			
			// Crea cinco hombres y cinco mujeres
			for(int i=0; i<5; i++){
				new Persona(i, true, libre).start();
				new Persona(i, false, libre).start();
			}
	}
}