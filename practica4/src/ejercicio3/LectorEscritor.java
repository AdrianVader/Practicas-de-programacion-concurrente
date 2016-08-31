package ejercicio3;





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
				monArb.entrarLeer(miNum); // Todos los hilos entran a leer
				monArb.entrarLeer(miNum);
				Thread.sleep(3500); // Esperamos para que se vea bien que solo escribe cuando es el último lector
				
				try {
					monArb.entrarEscribir(miNum); // Todos los hilos intentan escribir, pero solo el primero que llegue esperará hasta ser el último lector 
					monArb.entrarEscribir(miNum);
					baseDatos.escribir(miNum);
					System.out.println("Escritor " + miNum + " ecribe su numero");
					monArb.salirEscribir(miNum); // Sale de escritura
					monArb.salirEscribir(miNum);
					Thread.sleep(3500); // <- Le ponemos a esperar(opcionalmente para que de paso a lectores y no se produzca starving)
				} catch (Exception e){}
							
				if (Math.random()<0.5) Thread.sleep(2500); 
				System.out.println("Reader " + miNum + " reads " + baseDatos.leer());
				monArb.salirLeer(miNum);
				monArb.salirLeer(miNum); // Salimos de lectura
			}
		} catch (InterruptedException e){}
	}
	
}
