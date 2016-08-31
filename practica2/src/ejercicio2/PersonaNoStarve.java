package ejercicio2;




public class PersonaNoStarve extends Persona{

	static Semaphore semVar;
	static Semaphore antesalaChicas;
	static Semaphore antesalaChicos;
	static Semaphore semaforo;
	
	// Crea una persona sin inanición. En este caso utilizamos una antesala para que esperen fuera del baño
	PersonaNoStarve(int num, boolean mujer, Semaphore sem) {
		super(num, mujer, sem);
		if(antesalaChicas == null){antesalaChicas = new Semaphore(0);}
		if(semVar == null){semVar = new Semaphore(1);}
		if(antesalaChicos == null){antesalaChicos = new Semaphore(0);}
		if(semaforo == null){semaforo = new Semaphore(MAX);}
	}
	
	@Override
	public void run(){

		while(true){

			if(this.mujer){	//Código que ejecuta una mujer
				
				super.trabaja();
				
				try {semVar.P();} catch (InterruptedException e) {}
				if(CHICAS >= MAX){	// Si el baño está lleno espera en la antesala
					semVar.V();
					try {	antesalaChicas.P();	} catch (InterruptedException e) {}
				}
				semVar.V();
				
				// Comprueba la cantidad de chicas que hay en el baño
				try {semaforo.P();} catch (InterruptedException e1) {}
				
				// Si entra una chica no puede entrar ningún hombre
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
				
				// Si es la última en salir ya pueden entrar hombres
				disponibilidadMujeres.abrir_al_otro_genero_si_ultimo();
				semaforo.V();
				
				try {semVar.P();} catch (InterruptedException e) {}
				if(CHICAS <= 0){
					semVar.V();
					antesalaChicos.V();
				}
				semVar.V();
				
			}
			else{	//Código que ejecuta un hombre
				
				super.trabaja();
				
				try {semVar.P();} catch (InterruptedException e) {}
				if(CHICOS >= MAX){	// Si el baño está lleno espera en la antesala
					semVar.V();
					try {	antesalaChicos.P();	} catch (InterruptedException e) {}
				}
				semVar.V();
				
				// Comprueba la cantidad de chicas que hay en el baño
				try {semaforo.P();} catch (InterruptedException e1) {}
				
				// Si entra una chica no puede entrar ningún hombre
				disponibilidadHombres.cerrar_al_otro_genero_si_primero();

				System.out.println("Chico " + mi_numero + " entrando.");
				try {semVar.P();} catch (InterruptedException e) {}
					CHICOS++;
				semVar.V();
				
				super.utilizarServicios();
				
				try {semVar.P();} catch (InterruptedException e) {}
					CHICOS--;
				semVar.V();
				System.out.println("Chico " + mi_numero + " saliendo.");
				
				// Si es el último en salir ya pueden entrar mujeres
				disponibilidadHombres.abrir_al_otro_genero_si_ultimo();
				semaforo.V();
				
				try {semVar.P();} catch (InterruptedException e) {}
				if(CHICOS <= 0){
					semVar.V();
					antesalaChicas.V();
				}
				semVar.V();
				
			}
			
		}
		
	}

}
