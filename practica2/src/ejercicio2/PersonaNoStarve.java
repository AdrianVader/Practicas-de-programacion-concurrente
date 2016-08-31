package ejercicio2;




public class PersonaNoStarve extends Persona{

	static Semaphore semVar;
	static Semaphore antesalaChicas;
	static Semaphore antesalaChicos;
	static Semaphore semaforo;
	
	// Crea una persona sin inanici�n. En este caso utilizamos una antesala para que esperen fuera del ba�o
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

			if(this.mujer){	//C�digo que ejecuta una mujer
				
				super.trabaja();
				
				try {semVar.P();} catch (InterruptedException e) {}
				if(CHICAS >= MAX){	// Si el ba�o est� lleno espera en la antesala
					semVar.V();
					try {	antesalaChicas.P();	} catch (InterruptedException e) {}
				}
				semVar.V();
				
				// Comprueba la cantidad de chicas que hay en el ba�o
				try {semaforo.P();} catch (InterruptedException e1) {}
				
				// Si entra una chica no puede entrar ning�n hombre
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
				
				// Si es la �ltima en salir ya pueden entrar hombres
				disponibilidadMujeres.abrir_al_otro_genero_si_ultimo();
				semaforo.V();
				
				try {semVar.P();} catch (InterruptedException e) {}
				if(CHICAS <= 0){
					semVar.V();
					antesalaChicos.V();
				}
				semVar.V();
				
			}
			else{	//C�digo que ejecuta un hombre
				
				super.trabaja();
				
				try {semVar.P();} catch (InterruptedException e) {}
				if(CHICOS >= MAX){	// Si el ba�o est� lleno espera en la antesala
					semVar.V();
					try {	antesalaChicos.P();	} catch (InterruptedException e) {}
				}
				semVar.V();
				
				// Comprueba la cantidad de chicas que hay en el ba�o
				try {semaforo.P();} catch (InterruptedException e1) {}
				
				// Si entra una chica no puede entrar ning�n hombre
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
				
				// Si es el �ltimo en salir ya pueden entrar mujeres
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
