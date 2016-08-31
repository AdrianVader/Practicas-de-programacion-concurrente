package ejercicio2;



public class LectoresEscritores {

	static int numLectores = 4; 
	static int numEscritores = 2; 

	public static void main(String[] argv) {
 
		BaseDatos base = new BaseDatos();
		ReentranteEscritura mon = new ReentranteEscritura();

		try {
			for (int i = 0; i < numLectores; ++i) 
				new LectorReentrante(mon, base ,i).start();
			for (int i = 0; i < numEscritores; ++i) 
				new EscritorReentrante(mon, base, i+numLectores).start();
		} catch (Exception e) {System.out.println("Exception: " + e);}
	}
	
}





