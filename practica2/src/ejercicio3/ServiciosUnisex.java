package ejercicio3;


public class ServiciosUnisex{

	// Semáforo que se utiliza para controlar el acceso a la base de datos
	static final java.util.concurrent.Semaphore libre = new java.util.concurrent.Semaphore(1);

		public static void main(String[] args){
		
		if(args[0].equals("starve")){
			
			// Crea cinco hombres y cinco mujeres
			for(int i=0; i<5; i++){
				new Persona2(i, true, libre).start();
				new Persona2(i, false, libre).start();
			}
			
		}else if(args[0].equals("nostarve")){
			
			// Crea cinco hombres y cinco mujeres
			for(int i=0; i<5; i++){
				new PersonaNoStarve2(i, true, libre).start();
				new PersonaNoStarve2(i, false, libre).start();
			}
			
		}else{
			System.out.println("No sé que es: "+args[0]);
			System.exit(-1);
		}
		
	}
}