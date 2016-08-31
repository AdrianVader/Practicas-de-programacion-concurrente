package ejercicio2;




public class EscritorReentrante extends Thread {

	private MonitorArbitraje monArb;
	private BaseDatos baseDatos;
	private int miNum;

	EscritorReentrante(MonitorArbitraje mon, BaseDatos base, int num) {
		monArb = mon;
		baseDatos = base;
		miNum = num;
	}

	public void run() {
		try {
			while (true) {
				monArb.entrarEscribir(miNum); //<-Pedimos entrar a escribir por priera vez
				Thread.sleep(3500); 
				monArb.entrarEscribir(miNum); //<-Pedimos entrar a escribir por segunda vez
				if (Math.random()<0.5) Thread.sleep(2500); 
				baseDatos.escribir(miNum); //<-Escribimos nuestro número
				System.out.println("Escritor " + miNum + " ecribe su numero");
				monArb.salirEscribir(miNum); //<-Pedimos salir de escribir por primera vez
				monArb.salirEscribir(miNum); //<-Pedimos salir de escribir por segunda vez
				Thread.sleep(7500); // <- Le ponemos a esperar(opcionalmente para que de paso a lectores y no se produzca starving) 
			}
		} catch (InterruptedException e){}
	}
	
}