package ejercicio2;


public class ServiciosUnisex{

	// Sem�foro que se utiliza para controlar el acceso a la base de datos
	static final Semaphore libre = new Semaphore(1);

		public static void main(String[] args){
		
		if(args[0].equals("starve")){
			
			// Crea cinco hombres y cinco mujeres
			for(int i=0; i<5; i++){
				new Persona(i, true, libre).start();
				new Persona(i, false, libre).start();
			}
			
		}else if(args[0].equals("nostarve")){
			
			// Crea cinco hombres y cinco mujeres
			for(int i=0; i<5; i++){
				new PersonaNoStarve(i, true, libre).start();
				new PersonaNoStarve(i, false, libre).start();
			}
			
		}else{
			System.out.println("No s� que es: "+args[0]);
			System.exit(-1);
		}
		
	}
}