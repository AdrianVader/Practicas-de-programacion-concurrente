package ejercicio3;

public class PersonaNoStarve2 extends Persona2{

	static java.util.concurrent.Semaphore semVar;
	static java.util.concurrent.Semaphore antesalaChicas;
	static java.util.concurrent.Semaphore antesalaChicos;
	static java.util.concurrent.Semaphore semaforo;
	
	// Crea una persona sin inanición. En este caso utilizamos una antesala para que esperen fuera del baño
	PersonaNoStarve2(int num, boolean mujer, java.util.concurrent.Semaphore sem) {
		super(num, mujer, sem);
		if(antesalaChicas == null){antesalaChicas = new java.util.concurrent.Semaphore(1);}
		if(semVar == null){semVar = new java.util.concurrent.Semaphore(1);}
		if(antesalaChicos == null){antesalaChicos = new java.util.concurrent.Semaphore(1);}
		if(semaforo == null){semaforo = new java.util.concurrent.Semaphore(4);}
	}
	
	@Override
	public void run(){

		while(true){

			if(this.mujer){	//Código que ejecuta una mujer
				
				super.trabaja();
				
				try {semVar.acquire();} catch (InterruptedException e) {}
				if(CHICAS >= MAX){	// Si el baño está lleno espera en la antesala
					semVar.release();
					try {	antesalaChicas.acquire();	} catch (InterruptedException e) {}
				}
				semVar.release();
				
				// Comprueba la cantidad de chicas que hay en el baño
				try {semaforo.acquire();} catch (InterruptedException e1) {}
				
				// Si entra una chica no puede entrar ningún hombre
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
				
				// Si es la última en salir ya pueden entrar hombres
				disponibilidadMujeres.abrir_al_otro_genero_si_ultimo();
				semaforo.release();
				
				try {semVar.acquire();} catch (InterruptedException e) {}
				if(CHICAS <= 0){
					semVar.release();
					antesalaChicos.release();
				}
				semVar.release();
				
			}
			else{	//Código que ejecuta un hombre
				
				super.trabaja();
				
				try {semVar.acquire();} catch (InterruptedException e) {}
				if(CHICOS >= MAX){	// Si el baño está lleno espera en la antesala
					semVar.release();
					try {	antesalaChicos.acquire();	} catch (InterruptedException e) {}
				}
				semVar.release();
				
				// Comprueba la cantidad de chicas que hay en el baño
				try {semaforo.acquire();} catch (InterruptedException e1) {}
				
				// Si entra una chica no puede entrar ningún hombre
				disponibilidadHombres.cerrar_al_otro_genero_si_primero();

				System.out.println("Chico " + mi_numero + " entrando.");
				try {semVar.acquire();} catch (InterruptedException e) {}
					CHICOS++;
				semVar.release();
				
				super.utilizarServicios();
				
				try {semVar.acquire();} catch (InterruptedException e) {}
					CHICOS--;
				semVar.release();
				System.out.println("Chico " + mi_numero + " saliendo.");
				
				// Si es el último en salir ya pueden entrar mujeres
				disponibilidadHombres.abrir_al_otro_genero_si_ultimo();
				semaforo.release();
				
				try {semVar.acquire();} catch (InterruptedException e) {}
				if(CHICOS <= 0){
					semVar.release();
					antesalaChicas.release();
				}
				semVar.release();
				
			}
		}
		
	}

}
