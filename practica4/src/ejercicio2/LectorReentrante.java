package ejercicio2;



public class LectorReentrante extends Thread {

	private MonitorArbitraje monArb;
	private BaseDatos baseDatos;
	private int miNum;

	LectorReentrante(MonitorArbitraje mon, BaseDatos base, int num) {
		monArb = mon;
		baseDatos = base;
		miNum = num;
	}

	public void run() {
		try {
			while (true) {
				monArb.entrarLeer(miNum); //<-entramos a leer 1� vez
				//monArb.entrarLeer(miNum);//<-otros sitios desde los que se puede reentrar
				Thread.sleep(3500);
				//monArb.entrarLeer(miNum);//<-otros sitios desde los que se puede reentrar
				if (Math.random()<0.5) Thread.sleep(2500);
				//monArb.entrarLeer(miNum);//<-otros sitios desde los que se puede reentrar
				System.out.println("Reader " + miNum + " reads " + baseDatos.leer());
				monArb.entrarLeer(miNum); //<-entramos a leer 2� vez
				
				monArb.salirLeer(miNum); //<-salimos de leer 1� vez
				monArb.salirLeer(miNum); //<-salir de leer 2� vez
			}
		} catch (InterruptedException e){}
	}
	
}
