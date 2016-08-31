package ejercicio4;




public class LectoresEscritoresReentrantes {

	static int numLectoresEscritores = 4; 
	//static int numEscritores = 2; 

	public static void main(String[] argv) {
 
		BaseDatos base = new BaseDatos();
		ReentranteTotal mon = new ReentranteTotal();

		try {
			for (int i = 0; i < numLectoresEscritores; ++i) // Ya solo tenemos un tipo de hilo 
				new LectorEscritor(mon, base ,i).start();
		} catch (Exception e) {System.out.println("Exception: " + e);}
	}
	
}