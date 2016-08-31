package ejercicio4;





public class LectorEscritor extends Thread {

	private MonitorArbitraje monArb;
	private BaseDatos baseDatos;
	private int miNum;

	LectorEscritor(MonitorArbitraje mon, BaseDatos base, int num) {
		monArb = mon;
		baseDatos = base;
		miNum = num;
	}

	public void run() {
		try {
			while (true) {
				
				
				
				monArb.entrarEscribir(miNum);
				monArb.entrarEscribir(miNum);
				Thread.sleep(3500);	//<-Mientras voy a escribir me quiero poner a leer(lo que está escrito antes).
				
				monArb.entrarLeer(miNum);
				monArb.entrarLeer(miNum);
				System.out.println("Reader " + miNum + " reads " + baseDatos.leer());
				
				monArb.entrarEscribir(miNum);
				monArb.entrarEscribir(miNum);
				Thread.sleep(3500);
				baseDatos.escribir(miNum);
				System.out.println("Escritor " + miNum + " ecribe su numero");
				monArb.salirEscribir(miNum);
				monArb.salirEscribir(miNum);
				
				monArb.salirLeer(miNum);
				monArb.salirLeer(miNum);
				
				if (Math.random()<0.5) Thread.sleep(2500); 
				baseDatos.escribir(miNum);
				System.out.println("Escritor " + miNum + " ecribe su numero");
				monArb.salirEscribir(miNum);
				monArb.salirEscribir(miNum);
				if (Math.random()<0.5) Thread.sleep(2500);
				
				
			}
		} catch (InterruptedException e){}
	}
	
}
